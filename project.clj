(defproject rusty_fisher "0.1.0-SNAPSHOT"
  :description "A simple fishing script"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]]
  :repositories {"local" "file:repo"}
  :main javaCode.javaShim
  :target-path "target/%s"
  :*source-paths ["src/rusty_fisher"]
  :java-source-paths ["src/javaCode"]
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
             :provided {:dependencies [[local/client "1.0.0"]]}})
