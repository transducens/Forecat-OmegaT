Forecat plugin for OmegaT

Includes code from [fannj](https://github.com/krenfro/fannj)

Compile with 

```
ant jar
```

(or download from the build folder) and copy build/jar/forecat-omegat.jar and the contents of lib into the plugins folder of OmegaT.



###Neural network ranker
For using the neural network ranker that uses fannj, you should have a compiled version of [fann](http://leenissen.dk/), and run OmegaT using

```
-Djna.library.path=/usr/local/lib -Djna.nosys=true
```

replacing /usr/local/lib with the folder that contains libfann.so.

If, for some reason, you are unable to start OmegaT, and it reports a JNA exception, you will have to modify your omegat.prefs file, replacing

```
    <forecat_suggestion_ranker>neural</forecat_suggestion_ranker>
```

with 

```
    <forecat_suggestion_ranker>heuristic</forecat_suggestion_ranker>
```

Instructions for building your own neural network models can be found in [Forecat](https://github.com/transducens/forecat).
