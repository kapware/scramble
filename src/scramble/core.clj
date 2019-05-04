(ns core)

(defn fmap [f m]
  (into (empty m) (for [[k v] m] [k (f v)])))


(defn scramble? [letters a-word]
  (if (> (count letters) (count a-word))
    (not-any? neg?
              (vals
               (merge-with +
                           (frequencies letters)
                           (fmap (partial * -1)
                                 (frequencies a-word)))))
    false)) 


(comment
  (true? (scramble? "rekqodlw" "world")) 
  (false? (scramble? "" "foo")) 
  ;; Approach 1 diff
  (clojure.data/diff ["a" "b" "b" "c" "c"] ["b" "b" "b" "c"]) 
  (seq "abv") 
  (sort "zab") 
  (= (sort "oof") (last (clojure.data/diff (sort "fooz") (sort "oof")))) 
  (= (sort "world") (last (clojure.data/diff (sort "rekqodlw") (sort "world")))) 
  (clojure.data/diff (sort "rekqodlw") (sort "world")) 
  (clojure.data/diff (sort "rqodlw")
             (sort "world") ) 
  ;; Unsuccessful

  ;; Approach 2: frequencies
  (merge-with -
              (frequencies "aworld")
              (frequencies "worlds"))
  ;; Almost good, but it won't work for letters that are not in the dictionary

  ;; Let's flip the second collection then and use plus
  (merge-with +
              {:a 1 :b 1}
              {:b -2})
  
  (merge-with +
              (frequencies "aworld")
              (fmap (partial * -1) (frequencies "worlds")))

  (not-any? neg?
        (vals
         (merge-with +
                     (frequencies "rekqodlw") 
                     (fmap (partial * -1)
                           (frequencies "world")) )) )  
)
