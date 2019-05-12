(ns core-test
  (:require [clojure.test                            :as t]
            [scramble.scramble                       :as sut]
            [clojure.spec.alpha                      :as s]
            [com.gfredericks.test.chuck.clojure-test :as tc]
            [clojure.test.check.generators           :as gen]))

(t/deftest ^:unit check-spec-with-some-examples
  (t/testing "scamblie examples"
    (t/are [expected arg] (= expected (s/valid? ::sut/scramblie arg))
      ;; Make sure the examples from the requirements pass:
      true  "rekqodlw"
      true  "world"
      true  "cedewaraaossoqqyt"
      true  "codewars"
      true  "katas"
      true  "steak"
      ;; some mentioned (in requriements) edge cases:
      false "aa.aaa!" ;; punctuations
      false "22aa4" ;; digits
      ;; some obvious invalid scramblies:
      false 1
      false "AZ"
      false [2 1]
      false {:foo :bar}
      ;; but let's exclude empty strings and nils
      false  ""
      false  nil
      )))


;; For the sake of testing, define a subanagram tuple
(s/def ::subanagram (s/with-gen
                      (s/tuple ::sut/scramblie ::sut/scramblie)
                      #(gen/fmap
                        (fn [[scramblie noise]]
                          [(apply str ((fnil shuffle []) (seq (str scramblie noise))))
                           scramblie])
                        (gen/tuple
                         (s/gen ::sut/scramblie)
                         (s/gen ::sut/scramblie)))))


(t/deftest ^:unit scramble-detects-subanagrams
  (tc/checking "test scramble detects subanagrams" 1024
               [[word alphabet] (s/gen ::subanagram)]
               (t/is (true? (sut/scramble? word alphabet)))))
