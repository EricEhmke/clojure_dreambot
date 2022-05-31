(defproject dreambot_test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [local/client "1.0.0"]
                 [net.mikera/core.matrix "0.62.0"]]
  :repositories {"local" "file:repo"}
  :main javacode.TestScript
  :aot [dreambot-test.banknode dreambot-test.fishnode dreambot-test.travelnode]
  :target-path "target/%s"
  :*source-paths ["src/dreambot_test"]
  :java-source-paths ["src/javacode"]
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
