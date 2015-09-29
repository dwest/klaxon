(set-env!
 :source-paths #{"src/"}
 :dependencies '[[org.clojure/clojure "1.7.0"]
                 [ring "1.4.0"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [org.immutant/immutant "2.1.0"]
                 [commons-daemon "1.0.15"]
                 [clj-http "2.0.0"]
                 [clojurewerkz/quartzite "2.0.0"]])

(require '[klaxon.web.startup :as web]
         '[klaxon.daemon.status :as status])

(task-options!
 pom {:project 'klaxon
      :version "0.0.1"}
 aot {:all true
      :namespace '#{klaxon.web.startup}}
 jar {:manifest {"bin" "klaxon"}
      :main 'klaxon.web.startup})

(deftask build
  "Build the project"
  []
  (comp (aot) (pom) (uber) (jar)))

(deftask run
  "Run the project directly"
  []
  (web/start))

(deftask buildd
  "Build the daemon"
  []
  (task-options!
   aot {:all true
        :namespace '#{klaxon.daemon.status}}
   jar {:file "klaxon-node.jar"
        :manifset {"bin" "klaxon"}
        :main 'klaxon.daemon.status})
  (comp (aot) (pom) (uber) (jar)))

(deftask rund
  "Run the daemon directly"
  []
  (task-options!
   aot {:all true
        :namespace '#{klaxon.daemon.status}}
   jar {:manifset {"bin" "klaxon"}
        :main 'klaxon.daemon.status})
  (status/-main))
   

