(defproject c4-async "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/core.async "1.2.603"]
                 [clojure-lanterna "0.9.7"]
                 [jline/jline "2.14.6"]
                 [com.stuartsierra/component "1.0.0"]
                 [org.clojure/clojure "1.10.1"]]
  :repl-options {:init-ns c4-async.core}
  :main c4-async.core
  :plugins [[cider/cider-nrepl "0.24.0"]])
