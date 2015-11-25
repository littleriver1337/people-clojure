(ns people-clojure.core
  (:require [clojure.string :as str]
            [clojure.walk :as walk]
            [ring.adapter.jetty :as jetty]
            [hiccup.core :as h])
  (:gen-class))
(defn read-people []
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
    #_(spit "filtered_peope.edn"
          (pr-str people))
    people))

(defn people-html []
  (let [people (read-people)]
    (map (fn [line]
           [:p
            (str (:first_name line)
                 ""
                 (:last_name line))])
         people)))

(defn handler [request]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (h/html [:html
                     [:body
                      [:a {:href "http://www.theironyard.com"}
                       "The Iron Yard"]
                      [:br]
                      [:b {:href "http://www.riseofagon.com"}
                       "Rise Of Agon"]
                      [:br]
                      (people-html)]])})

(defn -main [& args]
  (jetty/run-jetty #'handler {:port 3000 :join? false}))    ;#' makes refresh dynamic and uses latest version of that function