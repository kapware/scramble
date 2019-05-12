# Scramble

> Mr. Praline: It's not a palindrome! The palindrome of "Bolton" would be "Notlob"!! It don't work!!
> Owner: Well, what do you want?
> Mr. Praline: I'm not prepared to pursue my line of inquiry any longer as I think this is getting too silly!

source: http://montypython.50webs.com/scripts/Series_1/53.htm

This is a showcase clojure project. The objective is to show how to solve a simple anagram problem, verifying if two given strings are anagrams of each other.

By common dictionary definition:
> anagram
> noun [ C ] US ​  /ˈæn·əˌɡræm/
​>
> a word or phrase made by using the letters of another word or phrase in a different order:
> “Neat” is an anagram of “a net.”

source: https://dictionary.cambridge.org/dictionary/english/anagram

This solution is a variation of the anagram problem, and answers a question if a subset of letters can be used to form a given word.

**Let** _d_ and _x_ be a sequence of letters `a`-`z`
**Def** A function `scramble?` (_d_, _x_) -> `[true|false]` called _subanagram_ will be defined as follows:
       `true` - for all _d_ and _x_ of which  _d_'s letters can be arranged to form _d_, i.e. `sort(common(d, x))` = `sort(x)`, where `common` is definined as a function returning a string with only common letters in both `d` and `x`.
       `false` - otherwise

Note, that order is significant and repetitions of letrters can occur within both _d_ and _x_ letter sequences. 

## Requirements
The app contains a function `scramble?` that takes words (with only lower case laters `a`-`z`; specificaly no special characters, digits or punctuation marks will be included).
The function will be exposed as a web service with a simple ui that would connect to the exposed api.

## Prerequisites
`clj` installed: https://clojure.org/guides/getting_started

## Running locally
After:
```
clj -Arun
```
the webservice should start responding on `http://localhost:9999`.
There is built in swagger-ui included, but to get a quick response, you might want to:
```
curl 'http://localhost:9999/api/v1/scramble?dict=bolton&word=notlob'
```

## Deploying
This is service is not meant for production, but if you still want to deploy it, build a jar (or native image), follow one of following excellent howtos:
https://github.com/clojure/tools.deps.alpha/wiki/Tools#build-tool-integration

or, if you're lazy:
```
clj -Ajar
```
and:
```
java -cp scramble.jar clojure.main -m scramble.core
```

## Running tests

All:
```
clj -Atest
```
Only unit tests:

```
clj -Atest -i :unit
```

## Running benchmarks
```
clj -Abench
```

## Profiling
`taoensso/tufte` is used to profile core `scramble?` function. To run profiler, simply:
```
(require '[taoensso.tufte :as tufte :refer (defnp p profiled profile)])
(tufte/add-basic-println-handler! {})
(profile
       {}
         (scramble/scramble? dict word))
```
To see profiling info on stdout.


## License

Copyright © 2019 kapware.com

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
* 
