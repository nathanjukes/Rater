package Rater.Util;

import org.apache.logging.log4j.util.Strings;

import java.util.List;
import java.util.stream.Collectors;

public class FlatStructure {

    // Previously used to get the full structure of a component e.g. "GET: app/service/api"
    @Deprecated
    public static String getFlatStructure(List<String> dirs) {
        return dirs.stream().collect(Collectors.joining("/"));
    }
}
