(ns bench.run
  (:require [jmh.core       :as jmh]
            [clojure.edn    :as edn]
            [clojure.pprint :as pprint]))


(def bench-env
  {:benchmarks [{:name :simple :fn 'scramble.core/scramble? :args [:state/dictionary :state/word]}]
   :states     {:dictionary {:fn 'bench.utils/make-dict :args [:param/dict-length]}
                :word       {:fn 'bench.utils/make-word :args [:param/word-length]}}
   :params     {:dict-length 10000
                :word-length 10}})

(defn -main [& [arg]]
  (let [bench-opts {:type :quick
                    :params {:dict-length [100 10000]
                             :word-length [1 5 10]}}
        result     (jmh/run bench-env bench-opts)]
    (pprint/pprint result)
    (pprint/pprint (map #(select-keys % [:params :score]) result))))
