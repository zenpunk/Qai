/*
 * Copyright 2017 Qoan Software Association. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package qube.qai.services;

import qube.qai.procedure.Procedure;
import qube.qai.procedure.ProcedureConstants;

import java.util.Set;

/**
 * Created by rainbird on 12/22/15.
 */
public interface ProcedureRunnerInterface {

    void submitProcedure(Procedure procedure);

    ProcedureConstants.ProcedureState queryState(String uuid);

    Set<String> getStartedProcedures();

//    void processEvent(Procedure procedure, ProcedureError error);
//
//    void processEvent(Procedure procedure, ProcedureStarted started);
//
//    void processEvent(Procedure procedure, ProcedureEnded ended);
}
