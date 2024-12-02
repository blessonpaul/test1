import java.io.*;
import java.util.HashMap;
import java.util.Map;



public class FlowLogProcessor {
    public static final String flowLogDataFileName = "./src/flow_data.txt";
    public static final String lookupDataFileName = "./src/lookup.txt";

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Please pass location of flow data file and lookup data file as input");
            System.exit(-1);
        }
        FlowLogProcessor obj = new FlowLogProcessor();
        try {
            Map<String, Integer> flowDataMap = new HashMap<>();
            Map<String, Integer> flowDataMapDstPort = new HashMap<>();
            Map<String, String> lookDataMap = new HashMap<>();

            obj.loadFlowDataFile(args[0], flowDataMap, flowDataMapDstPort);
            obj.loadLookupDataFile(args[1], lookDataMap);

            obj.printPortProtocol(flowDataMap);
            obj.printFlowDataToLookupData(flowDataMapDstPort, lookDataMap);


        } catch  (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public Map<String, Integer> loadFlowDataFile(String fileName, Map<String, Integer> flowDataMap, Map<String, Integer> flowDataMapDstPort) throws Exception {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            for (String log; (log = reader.readLine()) != null;) {
                FlowLogData logData = FlowLogDataParser.parser(log);
                if (logData != null) {
                    flowDataMap.merge(FlowLogDataParser.flowDataDstKey(logData), 1, Integer::sum);
                    flowDataMap.merge(FlowLogDataParser.flowDataSrcKey(logData), 1, Integer::sum);
                    flowDataMapDstPort.merge(FlowLogDataParser.flowDataDstKey(logData), 1, Integer::sum);

                }

            }
            reader.close();
        } catch (FileNotFoundException fe) {
            System.out.println("Flow Data File not found");
            throw fe;
        } catch (IOException ie) {
            System.out.println("Unable to read Flow Data file");
            throw ie;
        }

        return flowDataMap;
    }




    public Map<String, String> loadLookupDataFile(String lookTableFile, Map<String, String> lookDataMap)  throws Exception  {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(lookTableFile)));
            for (String data; (data = reader.readLine()) != null;) {
                LookupData lookupData = LookupDataParser.parseLookup(data);
                if (lookupData != null) {
                    String key = lookupData.getDstport() + "," + lookupData.getProtocol();
                    lookDataMap.put(key, lookupData.getTag());
                }

            }
            reader.close();
        } catch (FileNotFoundException fe) {
            System.out.println("Lookup Data File not found");
            throw fe;
        } catch (IOException ie) {
            System.out.println("Unable to read Lookup Data file");
            throw ie;
        }
        return lookDataMap;
    }

    public void printPortProtocol(Map<String, Integer> flowDataMap) {
        System.out.println("------ Port/Protocol Combination Counts: ----");

        for(Map.Entry<String, Integer> entry: flowDataMap.entrySet()) {
            String[] fields = entry.getKey().split(",");
            if (fields.length == 2) {
                System.out.println(fields[0] + "," + fields[1] + "," + entry.getValue());
            }

        }

    }

    public void printFlowDataToLookupData(Map<String, Integer> flowDataMap, Map<String, String> lookDataMap ) {
        System.out.println("------ Count of matches for each tag, sample o/p shown below----");
        int untaggedCount = 0;
        Map<String, Integer> tagCounter = new HashMap<>();
        for(Map.Entry<String, Integer> entry: flowDataMap.entrySet()) {

            if (lookDataMap.containsKey(entry.getKey())) {
                String tagkey = lookDataMap.get(entry.getKey()).toLowerCase();
                if (tagCounter.containsKey(tagkey)) {
                    tagCounter.put(tagkey, tagCounter.get(tagkey) + entry.getValue());
                } else {
                    tagCounter.put(tagkey, entry.getValue());
                }

            } else {
                untaggedCount += entry.getValue();
            }


        }
        for(Map.Entry<String, Integer> entry: tagCounter.entrySet()) {
            System.out.println(entry.getKey() + "," +entry.getValue());
        }

        System.out.println("Untagged," +untaggedCount);
    }





}

class FlowLogData {
    private final String sourcePort;

    private final String dstPort;
    private final String protocol;

    public FlowLogData(String sourcePort, String dstPort, String protocol) {
        this.sourcePort = sourcePort;
        this.dstPort = dstPort;
        this.protocol = protocol;
    }

    public String getSourcePort() {
        return sourcePort;
    }

    public String getDstPort() {
        return dstPort;
    }

    public String getProtocol() {
        return protocol;
    }
}

class FlowLogDataParser {
    public static FlowLogData parser(String log) {
        if (log == null || log.isEmpty()) {
            System.out.println("Log line is empty");
            return null;
        }
        String[] fields = log.split(" ");

        if (fields.length != 14) {
            System.out.println("Log line formatting is not correct");
            return null;
        }
        return  new FlowLogData(fields[5], fields[6], fields[7]);


    }

    public static String flowDataDstKey(FlowLogData data) {
        return data.getDstPort() + "," + getProtocolString(data.getProtocol());
    }

    public static String flowDataSrcKey(FlowLogData data) {
        return data.getSourcePort() + "," + getProtocolString(data.getProtocol());
    }

    public static String getProtocolString(String protocol) {

        switch(protocol) {
            case "6":
                return "tcp";
            case "1":
                return "icmp";
            case "17":
                return "udp";
            default:
                return "Unmapped";

        }
    }

}

class LookupData {

    private final String dstport;

    private final String protocol;

    private final String tag;

    public LookupData(String dstport, String protocol, String tag) {
        this.dstport = dstport;
        this.protocol = protocol;
        this.tag = tag;
    }

    public String getDstport() {
        return dstport;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getTag() {
        return tag;
    }
}

class LookupDataParser {
    public static LookupData parseLookup(String lookupData) {
        if (lookupData == null || lookupData.isEmpty()) {
            System.out.println("Lookup Data line is empty");
            return null;
        }
        String[] fields = lookupData.split(",");

        if (fields.length != 3) {
            System.out.println("Lookup Data formatting is not correct");
            return null;
        }
        return new LookupData(fields[0], fields[1], fields[2]);


    }



}

