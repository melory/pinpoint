package com.navercorp.pinpoint.profiler.monitor.jmx;

import java.util.List;
import java.util.Objects;

/**
 * @author meilong.hml(梅熙)
 * @date 2019-06-27 16:21
 * @see
 * @since
 */
public class JmxInfo {
    private String name;
    private List<String> queries;


    public JmxInfo(String name, List<String> queries) {
        this.name = name;
        this.queries = queries;
    }


    public List<String> getQueries() {
        return queries;
    }

    public void setQueries(List<String> queries) {
        this.queries = queries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JmxInfo jmxInfo = (JmxInfo) o;
        return Objects.equals(name, jmxInfo.name) &&
                Objects.equals(queries, jmxInfo.queries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, queries);
    }

    @Override
    public String toString() {
        return "JmxInfo{" +
                "name='" + name + '\'' +
                ", queries=" + queries +
                '}';
    }
}
