(ns scramble.scramble
  (:require [clojure.spec.alpha            :as s]
            [clojure.test.check.generators :as gen]
            [clojure.core.reducers         :as r]
            [taoensso.tufte                :as tufte]))

(defn char-range [start end]
  (map char (range (int start) (inc (int end)))))


(defn gen-scramblie
  "Returns a generator of a ::scramblie with given length"
  [& n]
  (gen/fmap
   (fn [chars] (apply str (map char chars)))
   (apply gen/vector (gen/elements (char-range \a \z)) n)))


(s/def ::scramblie (s/with-gen
                     (s/and string?
                            #(re-matches #"[a-z]+" %))
                     #(gen-scramblie)))


;; NOTE: this function does not perform better than regular version
;;       even for large strings like 1M characters
#_(defn pfrequencies
  "Returns a map from distinct items in coll to the number of times
  they appear, applying given function to each count. Paralell version."
  [f coll]
  (persistent!
   (r/fold
    (r/monoid #(merge-with + (persistent! %1) (persistent! %2)) (constantly (transient {})))
    (fn [counts x]
      (assoc! counts x (f (get counts x 0))))
    coll))) 


(defn ffrequencies
  "Returns a map from distinct items in coll to the number of times
  they appear, applying given function to each count. This is `clojure.core/frequencies`
  version, but allows replacing regular `inc` function."
  [f coll]
  (persistent!
   (reduce (fn [counts x]
             (assoc! counts x (f (get counts x 0))))
           (transient {}) coll)))

(comment
  (ffrequencies inc [:a :a :b :c :d :d :d]) 
  )

(defn scramble? [letters a-word]
  (tufte/p :total-scramble?
           (if (>= (count letters) (count a-word))
             (not-any? neg?
                       (vals
                        (merge-with +
                                    (tufte/p :letter-freqs
                                             (ffrequencies inc letters))
                                    (tufte/p :word-freqs
                                             (ffrequencies dec a-word)))))
             false)))  


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
