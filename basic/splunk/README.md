Splunk Sample
==============

This example demonstrates the use of Splunk **Outbound** and **Inbound** Adapter.
As configuration example is used a xml file

## Running the sample

Start Splunk server and change references in application.yml file.

type from the root project

    $ gradlew :splunk:run

#### Using an IDE such as eclipse

Type from <spring-integration>/basic/splunk

	mvn eclipse:eclipse

import the project and run it **org.springframework.integration.samples.splunk.Application** like any other main

### Output

The application sends 10 "custom" SplunkEvent to a main index and the read all event with yesterday as earliest-time
the received message are printed on sysout with stream adapter

Program output:

Producer:

    [2015-11-22	21:16:04:546+0100 _bkt="main~2~783FBE3F-8AA4-4DE3-BC2C-CA406157661C" _cd="2:23" _serial="0" _raw="2015-11-22	18:38:08:923+0100 ean="164" severity="ALERT" email="mail@gmail.com" order_number="21501001010101"" splunk_server="fbalicchia-desktop" index="main" source="tinyMain" _indextime="1448213888" _subsecond=".923" linecount="1" _si="fbalicchia-desktop,main" host="127.0.0.1" _sourcetype="customMessage" sourcetype="customMessage" _time="2015-11-22T18:38:08.923+01:00"]
    [2015-11-22	21:16:04:546+0100 _bkt="main~2~783FBE3F-8AA4-4DE3-BC2C-CA406157661C" _cd="2:18" _serial="1" _raw="2015-11-22	18:38:08:822+0100 ean="62" severity="ALERT" email="mail@gmail.com" order_number="21501001010101"" splunk_server="fbalicchia-desktop" index="main" source="tinyMain" _indextime="1448213888" _subsecond=".822" linecount="1" _si="fbalicchia-desktop,main" host="127.0.0.1" _sourcetype="customMessage" sourcetype="customMessage" _time="2015-11-22T18:38:08.822+01:00"]
    [2015-11-22	21:16:04:546+0100 _bkt="main~2~783FBE3F-8AA4-4DE3-BC2C-CA406157661C" _cd="2:13" _serial="2" _raw="2015-11-22	16:26:50:770+0100 ean="194" severity="ALERT" email="mail@gmail.com" order_number="21501001010101"" splunk_server="fbalicchia-desktop" index="main" source="tinyMain" _indextime="1448206010" _subsecond=".770" linecount="1" _si="fbalicchia-desktop,main" host="127.0.0.1" _sourcetype="customMessage" sourcetype="customMessage" _time="2015-11-22T16:26:50.770+01:00"]
    [2015-11-22	21:16:04:546+0100 _bkt="main~2~783FBE3F-8AA4-4DE3-BC2C-CA406157661C" _cd="2:8" _serial="3" _raw="2015-11-22	16:26:50:670+0100 ean="42" severity="ALERT" email="mail@gmail.com" order_number="21501001010101"" splunk_server="fbalicchia-desktop" index="main" source="tinyMain" _indextime="1448206010" _subsecond=".670" linecount="1" _si="fbalicchia-desktop,main" host="127.0.0.1" _sourcetype="customMessage" sourcetype="customMessage" _time="2015-11-22T16:26:50.670+01:00"]
]

In this snippet  we see toString() of message that send from program to Splunk, you can see the custom fields included email, ean code ecc.ecc

Consumer:

    [2015-11-22	21:32:33:467+0100 _bkt="main~2~783FBE3F-8AA4-4DE3-BC2C-CA406157661C" _cd="2:158" _serial="0" _raw="2015-11-22	21:25:50:956+0100 ean="216" severity="ALERT" email="mail@gmail.com" order_number="21501001010101"" splunk_server="fbalicchia-desktop" index="main" _kv="1" source="tinyMain" _indextime="1448223950" _subsecond=".956" ean="216" linecount="1" _si="fbalicchia-desktop,main" host="127.0.0.1" _sourcetype="customMessage" sourcetype="customMessage" _time="2015-11-22T21:25:50.956+01:00"]
    [2015-11-22	21:32:33:467+0100 _bkt="main~2~783FBE3F-8AA4-4DE3-BC2C-CA406157661C" _cd="2:153" _serial="1" _raw="2015-11-22	21:25:50:936+0100 ean="504" severity="ALERT" email="mail@gmail.com" order_number="21501001010101"" splunk_server="fbalicchia-desktop" index="main" _kv="1" source="tinyMain" _indextime="1448223950" _subsecond=".936" ean="504" linecount="1" _si="fbalicchia-desktop,main" host="127.0.0.1" _sourcetype="customMessage" sourcetype="customMessage" _time="2015-11-22T21:25:50.936+01:00"]
    [2015-11-22	21:32:33:467+0100 _bkt="main~2~783FBE3F-8AA4-4DE3-BC2C-CA406157661C" _cd="2:148" _serial="2" _raw="2015-11-22	21:25:50:916+0100 ean="357" severity="ALERT" email="mail@gmail.com" order_number="21501001010101"" splunk_server="fbalicchia-desktop" index="main" _kv="1" source="tinyMain" _indextime="1448223950" _subsecond=".916" ean="357" linecount="1" _si="fbalicchia-desktop,main" host="127.0.0.1" _sourcetype="customMessage" sourcetype="customMessage" _time="2015-11-22T21:25:50.916+01:00"]

The layout of the message is the same but we found entry with ean code greater than '135'. Splunk support out of the box a good query languages where more info can be found at splunk project documentation


> Please note that Before use this example, please, verify that **jdk.tls.disabledAlgorithms=SSLv3** entry in **lib/security/java.security** is commenting out


