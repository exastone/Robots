## Multi-threaded Java Exercise

Goal is to simulate a fleet of (dumb) robot vaccums cleaning a room as an execise of multi-threaded synchronization.

- Each robot may clean one tile per program cycle.
- Each robot must run concurrently.
- Default movement of robot is counter-clockwise spiral outward.
- To ensure completion is possible, a robot is placed in center of room facing upward on program start.
- If collision occurs, program must terminate and print location of collision.
- If robot attempts to go outside boundary of room, motion of robot fallsback to travelling along wall in counter-clockwise direciton.

## Project Struture

```
├── robots.txt
├── room.txt
└── src
    ├── Helpers.java
    ├── Main.java
    ├── ReadFile.java
    ├── Robot.java
    └── RobotVacuumSimulator.java
```

## Build/Compile

From project root:
```sh
$ javac -d ./out -sourcepath ./src ./src/Main.java
```

## To Run

```sh
$ cd out
$ java Main
```
---

## Running Test Cases

After build, `/out/` is generated and contains Java class files. Place *room.txt* and *robots.txt* in the generated build directory.
To test different room/robot configurations, change *room.txt* and *robots.txt* in `/out/`.

---

`room.txt` consists of single line with odd integer `N` which is the x,y dimention of the square room.

`robots.txt` consists of an integer, `M` on the first line which is the number of robots generated.
Following the first line is `M` lines, each containing 2 integers (starting horizontal, vertical position) and 1 char (U,L,D,R) denoting the starting direction the robot is facing. e.g.

```
3
5 5 U
15 15 U
20 20 U
```

If you would like to test with only a single robot at the center of the room, robots.txt should contain `0` and nothing else.

---

Example output: (50ms thread sleep delay)
```
[Room Initialized]                              [Starting Position of Robots]
   | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8  -> y       | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8  -> y
0  | . | . | . | . | . | . | . | . | . |        0  | . | . | . | . | . | . | . | . | . |
1  | . | . | . | . | . | . | . | . | . |        1  | . | . | . | . | . | . | . | . | . |
2  | . | . | . | . | . | . | . | . | . |        2  | . | . | U | . | . | . | . | . | . |
3  | . | . | . | . | . | . | . | . | . |        3  | . | . | . | . | . | . | . | . | . |
4  | . | . | . | . | . | . | . | . | . |        4  | . | . | . | . | U | . | . | . | . |
5  | . | . | . | . | . | . | . | . | . |        5  | . | . | . | . | . | . | . | . | . |
6  | . | . | . | . | . | . | . | . | . |        6  | . | . | . | . | . | . | U | . | . |
7  | . | . | . | . | . | . | . | . | . |        7  | . | . | . | . | . | . | . | . | . |
8  | . | . | . | . | . | . | . | . | . |        8  | . | . | . | . | . | . | . | . | . |
x↓                                              x↓

--- ROOM CLEAN after 51 loop iterations ---

---End of simulation---
Printing final state of room after 51 loop iterations:
[Room View]                                     [Room View w/ robots]
   | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8  -> y       | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8  -> y
0  | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |        0  | ✓ | ✓ | ✓ | ✓ | L | ✓ | ✓ | L | ✓ |
1  | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |        1  | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
2  | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |        2  | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
3  | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |        3  | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
4  | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |        4  | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | U |
5  | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |        5  | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
6  | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |        6  | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
7  | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |        7  | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
8  | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |        8  | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
x↓                                              x↓

Final position of robots:
Robot (8,4) facing U
Robot (7,0) facing L
Robot (4,0) facing L
Execution Time: 2791 ms
```
