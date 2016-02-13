(defproject com.hypirion/bencode "0.1.0-SNAPSHOT"
  :description "Java implementation Bencode."
  :url "https://github.com/hyPiRion/java-bencode"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies []
  :source-paths []
  :java-source-paths ["src"]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :scm {:dir ".."}
  :aliases {"javadoc" ["shell" "javadoc" "-d" "javadoc/${:version}"
                       "-sourcepath" "src" "com.hypirion.bencode"]}
  :plugins [[lein-shell "0.5.0"]]
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[org.clojure/clojure "1.8.0"]]}})
