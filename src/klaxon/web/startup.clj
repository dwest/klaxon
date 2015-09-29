(ns klaxon.web.startup
  (:require [immutant.web :as web]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [klaxon.monitor.disk :as disk]
            [klaxon.monitor.memory :as memory]
            [klaxon.monitor.net :as net])
  (:use [hiccup.core]
        [hiccup.page])
  (:gen-class))

(defroutes app
  (GET "/" [] 
       (html [:h1 "Oi!"]))
  (GET "/monitor/df" []
       (str "<pre>" (with-out-str (clojure.pprint/pprint (disk/df))) "</pre>"))
  (GET "/monitor/du" []
       (str "<pre>" (with-out-str (clojure.pprint/pprint (disk/du "/var" true))) "</pre>"))
  (GET "/monitor/meminfo" []
       (str "<pre>" (with-out-str (clojure.pprint/pprint (memory/info))) "</pre>"))
  (GET "/monitor/ping" []
       (str "<pre>" (with-out-str (clojure.pprint/pprint (net/ping "heimdall"))) "</pre>"))
  (GET "/monitor/http-ok" []
       (str "<pre>" (with-out-str (clojure.pprint/pprint (net/http-ok "http://localhost"))) "</pre>"))
  (GET "/monitor/http" []
       (str "<pre>" (with-out-str (clojure.pprint/pprint (net/http-content "http://localhost"))) "</pre>")))


(defn start []
  (web/run-dmc 
   app
   {:port 1234}))

(defn stop []
  (web/stop))

(defn -main []
  (start))

