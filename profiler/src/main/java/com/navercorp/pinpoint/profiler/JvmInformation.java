/*
 * Copyright 2016 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.pinpoint.profiler;

import com.navercorp.pinpoint.profiler.monitor.metric.gc.JvmGcType;

/**
 * @author HyunGil Jeong
 */
public class JvmInformation {

    private final String jvmVersion;
    private final JvmGcType jvmGcType;

    public JvmInformation(String jvmVersion, JvmGcType jvmGcType) {
        this.jvmVersion = jvmVersion;
        this.jvmGcType = jvmGcType;
    }

    public String getJvmVersion() {
        return jvmVersion;
    }

    public JvmGcType getJvmGcType() {
        return jvmGcType;
    }

    @Override
    public String toString() {
        return "JvmInformation{" +
                "jvmVersion='" + jvmVersion + '\'' +
                ", jvmGcType=" + jvmGcType.toString() +
                '}';
    }
}
