# growing neural gas

Scala implementation of supervised and unsupervised growing neural gas algorithms based on
- https://www.cs.swarthmore.edu/~meeden/DevelopmentalRobotics/fritzke95.pdf
- http://www.booru.net/download/MasterThesisProj.pdf

As example minist classification is implemented with supervised growing neural gas.

To install:

1. git clone https://github.com/jonysugianto/gng

2. Create folder /data/tmp at your local computer.

3. a) cd gng
   b) mvn clean compile assembly:single
   
4. java -classpath growing_neural_gas-1.0-SNAPSHOT-jar-with-dependencies.jar js.gng.LearnConfig

5. java -classpath growing_neural_gas-1.0-SNAPSHOT-jar-with-dependencies.jar js.minist.InitialTraining

