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

package qube.qai.procedure;

import qube.qai.parsers.antimirov.nodes.BaseNode;
import qube.qai.parsers.antimirov.nodes.ConcatenationNode;
import qube.qai.parsers.antimirov.nodes.Name;

/**
 * Created by rainbird on 3/30/17.
 */
public class ProcedureResults extends ConcatenationNode {

    public static String NAME = "Procdure Results";

    public ProcedureResults() {
        this.name = new Name(NAME);
    }

    public void addResult(BaseNode result) {
        if (getFirstChild() == null) {
            setFirstChild(result);
        } else if (getSecondChild() == null) {
            setSecondChild(result);
        } else {
            BaseNode tmp = getSecondChild();
            setSecondChild(new ConcatenationNode(tmp, result));
        }
    }
}
