/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sis.referencing.operation.matrix;

import java.util.Arrays;
import org.opengis.referencing.operation.Matrix;
import org.apache.sis.util.ArgumentChecks;
import org.apache.sis.util.ArraysExt;
import org.apache.sis.math.MathFunctions;
import org.apache.sis.internal.util.DoubleDouble;


/**
 * A two dimensional array of numbers. Row and column numbering begins with zero.
 *
 * {@section Support for extended precision}
 * This class can optionally support extended precision using the <cite>double-double arithmetic</cite>.
 * In extended precision mode, the {@link #elements} array have twice its normal length. The first half
 * of the array contains the same value than in normal precision mode, while the second half contains
 * the {@link DoubleDouble#error}.
 *
 * @author  Martin Desruisseaux (IRD, Geomatys)
 * @since   0.4 (derived from geotk-2.2)
 * @version 0.4
 * @module
 *
 * @see Matrices#createDiagonal(int, int)
 */
class GeneralMatrix extends MatrixSIS {
    /**
     * Serial number for inter-operability with different versions.
     */
    private static final long serialVersionUID = 8447482612423035360L;

    /**
     * All matrix elements in a flat, row-major (column indices vary fastest) array.
     * The array length is <code>{@linkplain #numRow} * {@linkplain #numCol}</code>.
     *
     * <p>In <cite>extended precision mode</cite>, the length of this array is actually twice the above-cited length.
     * The first half contains {@link DoubleDouble#value}, and the second half contains the {@link DoubleDouble#error}
     * for each value in the first half.</p>
     */
    final double[] elements;

    /**
     * Number of rows and columns.
     * This is non-final only for {@link NonSquareMatrix#transpose()} purpose.
     */
    short numRow, numCol;

    /**
     * Creates a matrix of size {@code numRow} × {@code numCol}.
     * If {@code setToIdentity} is {@code true}, then the elements
     * on the diagonal (<var>j</var> == <var>i</var>) are set to 1.
     *
     * @param numRow Number of rows.
     * @param numCol Number of columns.
     * @param setToIdentity {@code true} for initializing the matrix to the identity matrix,
     *        or {@code false} for leaving it initialized to zero.
     * @param precision 1 for normal precision, or 2 for extended precision.
     *        No other value is allowed (this is not verified).
     */
    GeneralMatrix(final int numRow, final int numCol, final boolean setToIdentity, final int precision) {
        ensureValidSize(numRow, numCol);
        this.numRow = (short) numRow;
        this.numCol = (short) numCol;
        elements = new double[numRow * numCol * precision];
        if (setToIdentity) {
            final int stop = Math.min(numRow, numCol) * numCol;
            for (int i=0; i<stop; i += numCol+1) {
                elements[i] = 1;
            }
        }
    }

    /**
     * Constructs a {@code numRow} × {@code numCol} matrix initialized to the values in the {@code elements} array.
     * The array values are copied in one row at a time in row major fashion.
     * The array shall be exactly {@code numRow*numCol} in length.
     *
     * @param numRow Number of rows.
     * @param numCol Number of columns.
     * @param elements Initial values.
     */
    GeneralMatrix(final int numRow, final int numCol, final double[] elements) {
        ensureValidSize(numRow, numCol);
        ensureLengthMatch(numRow * numCol, elements);
        this.numRow = (short) numRow;
        this.numCol = (short) numCol;
        this.elements = elements.clone();
    }

    /**
     * Constructs a new matrix and copies the initial values from the given matrix.
     *
     * @param matrix The matrix to copy.
     */
    GeneralMatrix(final Matrix matrix) {
        final int numRow = matrix.getNumRow();
        final int numCol = matrix.getNumCol();
        ensureValidSize(numRow, numCol);
        this.numRow = (short) numRow;
        this.numCol = (short) numCol;
        elements = new double[numRow * numCol];
        getElements(matrix, numRow, numCol, elements);
    }

    /**
     * Creates a clone of the given matrix, for {@link #clone()} usage only.
     */
    GeneralMatrix(final GeneralMatrix matrix) {
        numRow   = matrix.numRow;
        numCol   = matrix.numCol;
        elements = matrix.elements.clone();
    }

    /**
     * Creates a new extended precision matrix of the given size.
     *
     * @param numRow Number of rows.
     * @param numCol Number of columns.
     */
    static GeneralMatrix createExtendedPrecision(final int numRow, final int numCol) {
        if (numRow == numCol) {
            return new GeneralMatrix(numRow, numCol, false, 2);
        } else {
            return new NonSquareMatrix(numRow, numCol, false, 2);
        }
    }

    /**
     * Copies the elements of the given matrix in the given array.
     * This method ignores the error terms, if any.
     *
     * @param matrix   The matrix to copy.
     * @param numRow   The number of rows to copy (usually {@code matrix.getNumRow()}).
     * @param numCol   The number of columns to copy (usually {@code matrix.getNumCol()}).
     * @param elements Where to copy the elements.
     */
    private static void getElements(final Matrix matrix, final int numRow, final int numCol, final double[] elements) {
        if (matrix instanceof MatrixSIS) {
            ((MatrixSIS) matrix).getElements(elements);
        } else {
            for (int k=0,j=0; j<numRow; j++) {
                for (int i=0; i<numCol; i++) {
                    elements[k++] = matrix.getElement(j, i);
                }
            }
        }
    }

    /**
     * Infers all {@link DoubleDouble#error} with a default values inferred from {@link DoubleDouble#value}.
     * For example if a matrix element is exactly 3.141592653589793, there is good chances that the user's
     * intend was to specify the {@link Math#PI} value, in which case this method will infer that we would
     * need to add 1.2246467991473532E-16 in order to get a value closer to π.
     */
    private static void inferErrors(final double[] elements) {
        final int length = elements.length / 2;
        for (int i=length; i<elements.length; i++) {
            elements[i] = DoubleDouble.errorForWellKnownValue(elements[i - length]);
        }
    }

    /**
     * Returns the index of the first {@link DoubleDouble#error} value in the {@link #elements} array,
     * or 0 if none. This method returns a non-zero value only if the matrix has been created in extended
     * precision mode.
     */
    static int indexOfErrors(final int numRow, final int numCol, final double[] elements) {
        assert elements.length % (numRow * numCol) == 0;
        return (numRow * numCol) % elements.length; // A % B is for getting 0 without branching if A == B.
    }

    /**
     * Ensures that the given matrix size is valid for this {@code GeneralMatrix} implementation.
     */
    private static void ensureValidSize(final int numRow, final int numCol) {
        ArgumentChecks.ensureBetween("numRow", 1, Short.MAX_VALUE, numRow);
        ArgumentChecks.ensureBetween("numCol", 1, Short.MAX_VALUE, numCol);
    }

    /**
     * Returns the number of rows in this matrix.
     */
    @Override
    public final int getNumRow() {
        return numRow;
    }

    /**
     * Returns the number of columns in this matrix.
     */
    @Override
    public final int getNumCol() {
        return numCol;
    }

    /**
     * Retrieves the value at the specified row and column of this matrix.
     *
     * @param row    The row index, from 0 inclusive to {@link #getNumRow() } exclusive.
     * @param column The column index, from 0 inclusive to {@link #getNumCol()} exclusive.
     * @return       The current value at the given row and column.
     */
    @Override
    public final double getElement(final int row, final int column) {
        if (row >= 0 && row < numRow && column >= 0 && column < numCol) {
            return elements[row * numCol + column];
        } else {
            throw indexOutOfBounds(row, column);
        }
    }

    /**
     * Modifies the value at the specified row and column of this matrix.
     *
     * @param row    The row index, from 0 inclusive to {@link #getNumRow() } exclusive.
     * @param column The column index, from 0 inclusive to {@link #getNumCol()} exclusive.
     * @param value  The new value to set at the given row and column.
     */
    @Override
    public final void setElement(final int row, final int column, final double value) {
        if (row >= 0 && row < numRow && column >= 0 && column < numCol) {
            int i = row * numCol + column;
            elements[i] = value;
            i += numRow * numCol;
            if (i < elements.length) {
                elements[i] = DoubleDouble.errorForWellKnownValue(value);
            }
        } else {
            throw indexOutOfBounds(row, column);
        }
    }

    /**
     * Returns all elements of the given matrix followed by the error terms for extended-precision arithmetic.
     * The array will have twice the normal length. See {@link #elements} for more discussion.
     *
     * <p>This method may return a direct reference to the internal array. <strong>Do not modify.</strong>,
     * unless the {@code copy} argument is {@code true}.</p>
     *
     * @param copy If {@code true}, then the returned array is guaranteed to be a copy, never the internal array.
     */
    static double[] getExtendedElements(final Matrix matrix, final int numRow, final int numCol, final boolean copy) {
        double[] elements;
        final int length = numRow * numCol * 2;
        if (matrix instanceof GeneralMatrix) {
            elements = ((GeneralMatrix) matrix).elements;
            if (elements.length == length) {
                if (copy) {
                    elements = elements.clone();
                }
                return elements; // Internal array already uses extended precision.
            } else {
                elements = Arrays.copyOf(elements, length);
            }
        } else {
            elements = new double[length];
            getElements(matrix, numRow, numCol, elements);
        }
        inferErrors(elements);
        return elements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final double[] getElements() {
        return Arrays.copyOf(elements, numRow*numCol);
    }

    /**
     * Copies the matrix elements in the given flat array. This method does not verify the array length,
     * since the destination array may contain room for {@link DoubleDouble#error} terms.
     */
    @Override
    final void getElements(final double[] dest) {
        System.arraycopy(elements, 0, dest, 0, numRow*numCol);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setElements(final double[] newValues) {
        ensureLengthMatch(numRow*numCol, newValues);
        System.arraycopy(newValues, 0, elements, 0, newValues.length);
        if (elements.length != newValues.length) {
            inferErrors(newValues);
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>This method does not check the error terms, because those terms are not visible to the user
     * (they can not appear in the value returned by {@link #getElement(int, int)}, and are not shown
     * by {@link #toString()}) - returning {@code false} while the matrix clearly looks like affine
     * would be confusing for the user. Furthermore, the errors can be non-zero only in the very last
     * element and that value always smaller than 2.3E-16.</p>
     */
    @Override
    public final boolean isAffine() {
        final int numRow = this.numRow; // Protection against accidental changes.
        final int numCol = this.numCol;
        if (numRow == numCol) {
            int i = numRow * numCol;
            if (elements[--i] == 1) {
                final int base = (numRow - 1) * numCol;
                while (--i >= base) {
                    if (elements[i] != 0) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * <p>This method does not check the error terms, because those terms are not visible to the user
     * (they can not appear in the value returned by {@link #getElement(int, int)}, and are not shown
     * by {@link #toString()}) - returning {@code false} while the matrix clearly looks like identity
     * would be confusing for the user. Furthermore, the errors can be non-zero only on the diagonal,
     * and those values always smaller than 2.3E-16.</p>
     *
     * <p>An other argument is that the extended precision is for reducing rounding errors during
     * matrix arithmetics. But since the user provided the original data as {@code double} values,
     * the extra precision usually have no "real" meaning.</p>
     */
    @Override
    public final boolean isIdentity() {
        final int numRow = this.numRow; // Protection against accidental changes.
        final int numCol = this.numCol;
        if (numRow != numCol) {
            return false;
        }
        int di = 0; // Index of next diagonal element.
        final int length = numRow * numCol;
        for (int i=0; i<length; i++) {
            final double element = elements[i];
            if (i == di) {
                if (element != 1) return false;
                di += numCol + 1;
            } else {
                if (element != 0) return false;
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * The implementation provided by {@code GeneralMatrix} is valid only for square matrix.
     * {@link NonSquareMatrix} must override.
     */
    @Override
    public void transpose() {
        final int numRow = this.numRow; // Protection against accidental changes.
        final int numCol = this.numCol;
        final int errors = indexOfErrors(numRow, numCol, elements); // Where error values start, or 0 if none.
        for (int j=0; j<numRow; j++) {
            for (int i=0; i<j; i++) {
                final int lo = j*numCol + i;
                final int up = i*numCol + j;
                ArraysExt.swap(elements, lo, up);
                if (errors != 0) {
                    // Swap also the error terms in extended precision mode.
                    ArraysExt.swap(elements, lo + errors, up + errors);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>The current implementation discards the extended precision, if any.</p>
     */
    @Override
    public final void normalizeColumns() {
        final int numRow = this.numRow; // Protection against accidental changes.
        final int numCol = this.numCol;
        final double[] column = new double[numRow];
        for (int i=0; i<numCol; i++) {
            for (int j=0; j<numRow; j++) {
                column[j] = elements[j*numCol + i];
            }
            final double m = MathFunctions.magnitude(column);
            for (int j=0; j<numRow; j++) {
                elements[j*numCol + i] /= m;
            }
        }
        Arrays.fill(elements, numRow * numCol, elements.length, 0);
    }

    /**
     * Sets this matrix to the product of the given matrices: {@code this = A × B}.
     * The matrix sizes much match - this is not verified unless assertions are enabled.
     */
    final void setToProduct(final MatrixSIS A, final Matrix B) {
        final int numRow = this.numRow; // Protection against accidental changes.
        final int numCol = this.numCol;
        final int nc = A.getNumCol();
        assert B.getNumRow() == nc;
        assert numRow == A.getNumRow() && numCol == B.getNumCol();
        /*
         * Get the matrix element values, together with the error terms if the matrix
         * use extended precision (double-double arithmetic).
         */
        final double[] eltA   = getExtendedElements(A, numRow, nc, false);
        final double[] eltB   = getExtendedElements(B, nc, numCol, false);
        final int errorOffset = numRow * numCol; // Where error terms start.
        final int errA        = numRow * nc;
        final int errB        = nc * numCol;
        /*
         * Compute the product, to be stored directly in 'this'.
         */
        final DoubleDouble dot = new DoubleDouble();
        final DoubleDouble sum = new DoubleDouble();
        for (int k=0,j=0; j<numRow; j++) {
            for (int i=0; i<numCol; i++) {
                sum.clear();
                int iB = i;       // Index of values in a single column of B.
                int iA = j * nc;  // Index of values in a single row of A.
                final int nextRow = iA + nc;
                while (iA < nextRow) {
                    dot.setFrom (eltA, iA, errA);
                    dot.multiply(eltB, iB, errB);
                    sum.add(dot);
                    iB += numCol; // Move to next row of B.
                    iA++;         // Move to next column of A.
                }
                sum.storeTo(elements, k++, errorOffset);
            }
        }
    }

    /**
     * Returns {@code true} if the specified object is of type {@code GeneralMatrix} and
     * all of the data members are equal to the corresponding data members in this matrix.
     *
     * @param object The object to compare with this matrix for equality.
     * @return {@code true} if the given object is equal to this matrix.
     */
    @Override
    public final boolean equals(final Object object) {
        if (object instanceof GeneralMatrix) {
            final GeneralMatrix that = (GeneralMatrix) object;
            return numRow == that.numRow &&
                   numCol == that.numCol &&
                   Arrays.equals(elements, that.elements);
        }
        return false;
    }

    /**
     * Returns a hash code value based on the data values in this object.
     */
    @Override
    public final int hashCode() {
        return ((numRow << Short.SIZE) | numCol) ^ Arrays.hashCode(elements) ^ (int) serialVersionUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MatrixSIS clone() {
        return new GeneralMatrix(this);
    }
}
