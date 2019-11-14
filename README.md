# Online Material for VaMoS 2020

## PPR CPR Definition in BNF

```
<model>        ::= *<ppr-cpr>
<ppr-cpr>      ::= <process> | <input2output> | <resource>
<process>      ::= "('" <process-name> "','" <outputproduct-name> "')"
<input2output> ::= "('" <inputproduct-name> "','" <outputproduct-name> "')"
<resource>     ::= "('" <resource-name> "','" <process-name> "','" <inputproduct-name> "')"
<process-name> ::= <text>
<outputproduct-name> ::= <text>
<inputproduct-name>  ::= <text>
<resource-name>      ::= <text>
<text>         ::= %x22 *( %x20-21 / %x23-7E ) %x22
```
