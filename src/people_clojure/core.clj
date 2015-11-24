(ns people-clojure.core
  (:require [clojure.string :as str]
            [clojure.walk :as walk])
  (:gen-class))

(defn -main [& args]
  (let [people (slurp "people.csv")                           ;lol slurp reads the csv file
        people (str/split-lines people)         ;str is the clojure.string as an alias and /split-lines splits people
        people (map (fn [line]
                      (str/split line #","))                ;comma seperated values
                    people)
        header (first people)
        people (rest people)
        people (map (fn [line]
                      (interleave header line))
                    people)
        people (map (fn [line]
                      (apply hash-map line))
                    people)
        people (walk/keywordize-keys people)
        people (filter (fn [line]
                         (= "Brazil" (:country line)))
                       people)]
    (spit "filtered_peope.edn"
          (pr-str people))))