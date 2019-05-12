(ns scramble.core
  (:require [clojure.spec.alpha            :as s]
            [clojure.test.check.generators :as gen]))

(defn char-range [start end]
  (map char (range (int start) (inc (int end)))))


(s/def ::scramblie (s/with-gen
                     (s/and string?
                            #(re-matches #"[a-z]+" %))
                     #(gen/fmap
                       (fn [chars] (apply str (map char chars)))
                       (gen/vector (gen/elements (char-range \a \z))))))

  
(defn fmap [f m]
  (into (empty m) (for [[k v] m] [k (f v)])))


(defn scramble? [letters a-word]
  (if (>= (count letters) (count a-word))
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

  ;; Verify scramble? arg(s) domain
  (s/explain-str ::scramblie "AAA") 
  (s/explain-str ::scramblie "aaaa") 
  (s/explain-str ::scramblie "a.!") 
  (s/explain-str ::scramblie 1) 

  (clojure.string/lower-case "FOO") 
  (require '[clojure.test.check.generators :as gen]) 
  ;; Let's generate some scramblies reliably
  ;; ... but first, we need a proper generator
  (gen/sample (gen/vector (gen/elements (char-range \a \z))))
  (gen/sample (s/gen ::scramblie) 1000) 

  ;; Now we should be ready generate some test data for our function:
  ;; generate a pair of scramblies -> add some noise and shuffle
  (apply str (shuffle (seq (str "foo" "bar")))) 
  (def test-data (gen/sample (gen/fmap
                              (fn [[scramblie noise]]
                                [(apply str ((fnil shuffle []) (seq (str scramblie noise))))
                                 scramblie])
                              (gen/tuple
                               (s/gen ::scramblie)
                               (s/gen ::scramblie))) 1024)) 
  ;; Bump:
  (shuffle nil) ;; => NPE
  ;; solution:
  ((fnil shuffle []) nil) ;; => happy :)

  ;; Expected empty:
  (filter (fn [[_ _ result]] (false? result))
          (map (fn [[letters word]]
                 [letters word (scramble? letters word)])
                   test-data))  
)
