(defproject jainsahab/lein-githooks "1.0.1-SNAPSHOT"
  :description "Leiningen plugin for managing git client hooks"
  :url "https://github.com/jainsahab/lein-githooks"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :eval-in-leiningen true

  :signing {:gpg-key "prateek.jain23995@yahoo.com"}

  :profiles {:dev {:plugins  [[lein-githooks "0.1.0"]]
                   :githooks {:pre-push ["lein test"]}}})
