Forecat plugin for OmegaT

Includes code from [fannj](https://github.com/krenfro/fannj)

###OmegaT bug

In the last stable version (3.6.0.2) of [OmegaT](https://sourceforge.net/projects/omegat/) there is a bug that prevents the correct rendering of the suggestions on the Autocomplete popup. A temporal workaround (that breaks the rest of autocompleters) can be achieved patching Autocompleter.java with the following code

```
	@Override
	public void addView(AbstractAutoCompleterView view) {
		view.setParent(this);
		views.add(0,view);  // Instead of the original .add(view)
	}
```

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
