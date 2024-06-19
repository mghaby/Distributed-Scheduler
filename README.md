# First Capable (FC) & All to Largest (ATL) Scheduling Algorithms

## Description

The code within this repo is structured within 5 directories. Directories `tutorial1`, `tutorial2`, and `tutorial3`, contain the structured progress I have been making on the assorted distributed system tutorial work. The `fc` directory contains the final First Capable Scheduling Algorithm implementation. The `atl` directory contains the final All to Largest Scheduling Algorithm implementation. The server actor is provided by the `ds-sim` repo, which is a distributed system simulator and can be found in the acknowledgements section. 

The purpose of this repository is utlimately so showcase the implementation of the First Capable and All to Largest Scheduling Algorithms in a distributed system environment. The distrubuted nature of the algrithms are facilitated through socket programming.

## Getting Started

### Dependencies

* JDK 11
* Linux Environent

### Installing

* This repo can be cloned using:
```
git clone *repo link here*
```

### Executing program

1. Once cloned, path into the relevant directory you wish to test. 
2. You then must run `javac Client.java` on a terminal within that directory.
3. Open a seperate terminal that paths into the root directory of this repo and run `./ds-server -p 52584 -n -v all -c ___` where `___` is replaced by a valid xml config file.
4. In the original terminal, run `java Client` 

## Authors

1. Mark Ghaby
  * [Github](https://github.com/mghaby)
  * [LinkedIn](https://www.linkedin.com/in/mghaby/)

# License
This project is licensed under the GNU General Public License - see the LICENSE file for details

## Acknowledgments

* [README-Template](https://gist.github.com/DomPizzie/7a5ff55ffa9081f2de27c315f5018afc)
* [ds-sim](https://github.com/distsys-MQ/ds-sim)