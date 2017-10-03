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

package qube.qai.parsers.maths;

/**
 * Created by rainbird on 10/8/16.
 */

import junit.framework.TestCase;

public class MathParserTest extends TestCase {

    public void testEvaluate() {
        assertResult(1, "1");
        assertResult(1, "(1)");
        assertResult(3, "1+2");
        assertResult(-5, "1+2*-3");
        assertResult(1, "((1-2)/-1)");
        assertResult(10, "2 5");
        assertResult(3, "1 + 2 (4 - 3) /*comment*/");
    }

    private static void assertResult(int expected, String source) {
        assertTrue(expected == MathParser.CALCULATOR.parse(source));
    }

}