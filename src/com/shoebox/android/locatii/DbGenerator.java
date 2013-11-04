/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shoebox.android.locatii;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * 
 * Run it as a Java application (not Android).
 * 
 * @author Magda Badita 
 */
public class DbGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "de.greenrobot.daoexample");
 
        Entity product = schema.addEntity("Locatii");
        product.addIdProperty(); 
        product.addStringProperty("oras").notNull();
        product.addStringProperty("adresa");
        product.addStringProperty("latitudine");
        product.addStringProperty("longitudine");
        product.addStringProperty("detaliiAdresa");
        product.addStringProperty("persoanaContact1");
        product.addStringProperty("nrTelefonPersoanaContact1");
        product.addStringProperty("persoanaContact2");
        product.addStringProperty("nrTelefonPersoanaContact2");
        product.addStringProperty("orar");
 
   
        new DaoGenerator().generateAll(schema, "../ShoeBoxApp/src-gen");
    }
}
