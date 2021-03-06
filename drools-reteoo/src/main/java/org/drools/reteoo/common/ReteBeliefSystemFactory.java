/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.drools.reteoo.common;

import org.drools.core.BeliefSystemType;
import org.drools.core.beliefsystem.BeliefSystem;
import org.drools.core.beliefsystem.jtms.JTMSBeliefSystem;
import org.drools.core.beliefsystem.simple.SimpleBeliefSystem;
import org.drools.core.common.BeliefSystemFactory;
import org.drools.core.common.NamedEntryPoint;
import org.drools.core.common.TruthMaintenanceSystem;

import java.io.Serializable;

public class ReteBeliefSystemFactory implements BeliefSystemFactory, Serializable {

    public BeliefSystem createBeliefSystem(BeliefSystemType type, NamedEntryPoint ep,
                                           TruthMaintenanceSystem tms) {
        switch(type) {
            case SIMPLE:
                return new SimpleBeliefSystem(ep, tms);
            case JTMS:
                return new JTMSBeliefSystem( ep, tms );
            case DEFEASIBLE:
                throw new UnsupportedOperationException("Rete mode does not support Defeasible Belief Systems" );
        }
        throw new UnsupportedOperationException();
    }
}
