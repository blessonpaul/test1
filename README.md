How to compile
1) clone repo
2) cd src
3) javac FlowLogProcessor.java 

How to run
1) java FlowLogProcessor ../input/flow_data_large_file.txt ../input/lookup.txt

Tests
1) Incorrect file
   
   java FlowLogProcessor ../input/flow_data.txt ../input/lookup1.txt
      
   java FlowLogProcessor ../input/flow_data1.txt ../input/lookup1.txt
      
3) Empty file
   
   java FlowLogProcessor ../input/flow_data_no_data.txt ../input/lookup.txt
5) Positive test
6) 
   java FlowLogProcessor ../input/flow_data_large_file.txt ../input/lookup.txt
7) Duplicate flow records
   
   java FlowLogProcessor ../input/flow_data_same_record.txt ../input/lookup.txt
9) Large flow file
    
   java FlowLogProcessor ../input/flow_data_large_file.txt ../input/lookup.txt


Assumptions
1) Only flow data records with 14 columns are accepted
2) First input file should be flow record and second one should be lookup table
3) Only tcp, udp and icmp are mapped. Rest of the records will come under unmapped in Port/Protocol Combination
4) Unmapped in flow records will go under Untagged in Lookup matches
5) 
