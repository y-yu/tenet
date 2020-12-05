Tenet simulator HITCON CTF 2020
=============================
![CI](https://github.com/y-yu/tenet-simulator/workflows/CI/badge.svg?branch=master)

Cheep assembly language emulator to solve [Tenet in HITCON CTF 2020](https://ctftime.org/task/14033).

## Demo

Scala.js demo is here: http://y-yu.github.io/tenet-simulator

## How to run

```console
./sbt run
```

## Abstract of the problem: tenet

Note: the problem scenario is much simpler than original.   

1. A secret random number will be give as `EAX` register value
    - Other registers are all zero
2. Then we'll run arbitrary assembly codes starting with the (1) register state
3. When the code execution ends, the system accepts if `EAX` is zero
    - If `EAX` is not zero, the system will fail
4. The system will execute the code that is reverse ordered code executed in (2)
5. When the reverse ordered code execution ends, if `EAX` equals to the secret random number in (1) then the system accepts