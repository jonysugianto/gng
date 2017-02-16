# growing neural gas

Golang implementation of supervised and unsupervised growing neural gas algorithms based on
- https://www.cs.swarthmore.edu/~meeden/DevelopmentalRobotics/fritzke95.pdf
- http://www.booru.net/download/MasterThesisProj.pdf

As example minist classification is implemented with supervised growing neural gas.

To install:

1. go get github.com/jonysugianto/dfa_cnn

2. a) download minist dataset from http://yann.lecun.com/exdb/mnist/
   b) unzip all zip files and put in to the folder: /tmp/minist

3. a) cd minist_example/minist_mlnn (minist recognition using multi layer feed forward network only)

   b) go build

   c) ./minist_mlnn

4. a) cd minist_example/minist_cnn (minist recognition using multi layer convolutional neural network
                                   and multi layer feed forward network)

   b) go build

   c) ./minist_cnn