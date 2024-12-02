How to compile
1) clone repo
2) cd src
3) javac FlowLogProcessor.java 

How to run
1) java FlowLogProcessor ../input/flow_data_large_file.txt ../input/lookup.txt

Tests
1) Incorrect file
   2) java FlowLogProcessor ../input/flow_data.txt ../input/lookup1.txt
   3) java FlowLogProcessor ../input/flow_data1.txt ../input/lookup1.txt
2) Empty file
   3) java FlowLogProcessor ../input/flow_data_no_data.txt ../input/lookup.txt
3) Positive test
   4) java FlowLogProcessor ../input/flow_data_large_file.txt ../input/lookup.txt
4) Duplicate flow records
   5) java FlowLogProcessor ../input/flow_data_same_record.txt ../input/lookup.txt
5) Large flow file
   6) java FlowLogProcessor ../input/flow_data_large_file.txt ../input/lookup.txt


Assumptions
1) Only flow data records with 14 columns are accepted
2) First input file should be flow record and second one should be lookup table
3) Only tcp, udp and icmp are mapped. Rest of the records will come under unmapped in Port/Protocol Combination
4) Unmapped in flow records will go under Untagged in Lookup matches
5) 