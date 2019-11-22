# Online Material for VaMoS 2020

You can find the *CPR2FC* algorithm in the `FCI.java` and the *FC2SI* algorithm in the `SIModel.java` file.

## Build and Test

The code can be build with *Maven* by running `mvn clean install` with compiles the code and runs four test cases that create a file with a superimposed Product-Process-Resource model.

## PPR CPR Definition in BNF

```
<model>        ::= *<ppr-cpr>
<ppr-cpr>      ::= "('" <process> | <input2output> | <resource> "')"
<process>      ::= <process-name> "','" <outputproduct-name>
<input2output> ::= <inputproduct-name> "','" <outputproduct-name>
<resource>     ::= <resource-name> "','" <process-name> "','" <inputproduct-name>
<process-name> ::= <text>
<outputproduct-name> ::= <text>
<inputproduct-name>  ::= <text>
<resource-name>      ::= <text>
<text>         ::= %x22 *( %x20-21 / %x23-7E ) %x22
```
