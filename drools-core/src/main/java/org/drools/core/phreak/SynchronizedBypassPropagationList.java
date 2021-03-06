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

package org.drools.core.phreak;

import org.drools.core.common.InternalWorkingMemory;

import java.util.concurrent.atomic.AtomicBoolean;

public class SynchronizedBypassPropagationList extends SynchronizedPropagationList {

    private AtomicBoolean executing = new AtomicBoolean( false );

    public SynchronizedBypassPropagationList(InternalWorkingMemory workingMemory) {
        super(workingMemory);
    }

    @Override
    public void addEntry(final PropagationEntry propagationEntry) {
        doAddEntry( propagationEntry, false );
    }

    private void doAddEntry( final PropagationEntry propagationEntry, final boolean addToTop ) {
        workingMemory.getAgenda().executeTask( new ExecutableEntry() {
           @Override
           public void execute() {
               if (executing.compareAndSet( false, true )) {
                   try {
                       propagationEntry.execute( workingMemory );
                   } finally {
                       executing.set( false );
                       flush();
                   }
               } else {
                   doAdd();
               }
           }

           @Override
           public void enqueue() {
               doAdd();
           }

            private void doAdd() {
               if (addToTop) {
                   SynchronizedBypassPropagationList.super.addEntryToTop( propagationEntry );
               } else {
                   internalAddEntry( propagationEntry );
               }
           }
        });
        notifyHalt();
    }

    @Override
    public void addEntryToTop(PropagationEntry propagationEntry ) {
        doAddEntry( propagationEntry, true );
    }

    @Override
    public void flush() {
        if (!executing.get()) {
            super.flush();
        }
    }

    @Override
    public void onEngineInactive() {
        flush();
    }
}
