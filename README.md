# Scramble

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
