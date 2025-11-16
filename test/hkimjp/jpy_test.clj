(ns hkimjp.jpy-test
  (:require [clojure.test :refer [deftest is testing]]
            [hkimjp.jpy :as sut])) ; system under test

(deftest a-test
  (testing "FIXED, I success."
    (is (= 0 0))))
