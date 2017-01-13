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

/**
 * Miscellaneous objects and adapters defined in the {@code "gmd"} namespace.
 * This package does not include the adapters for ISO 19115 classes and code
 * lists (except base classes), which are defined in their own package.
 *
 * @author  Cédric Briançon (Geomatys)
 * @author	Cullen Rombach	(Image Matters)
 * @since   0.3
 * @version 0.8
 * @module
 *
 * see javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter
 */
@XmlSchema(elementFormDefault = XmlNsForm.QUALIFIED, namespace = Namespaces.MDB, xmlns = {
	@XmlNs(prefix = "mdb", namespaceURI = Namespaces.MDB),
    @XmlNs(prefix = "gco", namespaceURI = Namespaces.GCO)
})
@XmlAccessorType(XmlAccessType.NONE)
package org.apache.sis.internal.jaxb.gmd;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.apache.sis.xml.Namespaces;
