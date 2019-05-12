(ns bench.utils
  (:require [clojure.test.check.generators :as gen]
            [scramble.core :as scramble]))


;; What kind of sets needs to be considered?
;; - short
;; - dictionary (i.e. real words)
;; - real anagrams (i.e. length of two args are the same)
;; - long
;; - random
(defn make-dict [n]
  (gen/generate (scramble/gen-scramblie n)))


(defn make-word [n]
  (gen/generate (scramble/gen-scramblie n)))
