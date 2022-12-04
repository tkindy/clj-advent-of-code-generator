(ns dayXX-test
  (:require dayXX
            [clojure.test :refer [deftest is]]))

(def example (dayXX/parse-input (slurp "examples/dayXX.txt")))

(deftest TEMP_part1-name
  (is (= (dayXX/TEMP_part1-name example)
         TEMP_example-answer)))
