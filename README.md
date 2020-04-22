# lein-githooks

`lein-githooks` is a simple way to manage client-side git hooks for
[Leiningen](http://leiningen.org) projects.

Git hooks are great for automatically running test and quality checks
at key points in the git workflow.

Unfortunately, living as they do under `.git/hooks` rather than in the
main repository, they're not by default under source control so
they take a bit of effort to distribute to developers. Most people are
quite willing to have tests and quality checks run automatically
before push but it's a real pain having to manually install the hooks
necessary to do this.

How many times have you hit enter on a `git push` and suddenly
realised you had forgotten to run the complete test suite after your
last minor change? I do that sort of thing pretty frequently. Sure,
you probably have some comprehensive testing going on in CI tooling
anyway but it can save a lot of time and embarrassment to have git run
these things for you locally before they get that far.

Now you may like to configure these things yourself - that's all well
and good - or it might just be more convenient to have a consistent
set of hooks defined at project level. That's what `lein-githooks`
allows for leiningen projects.

Use lein-githooks if:

- you want to easily automate quality checks at key points in the git
    workflow and you'd like to define this hooks in your `project.clj`
    along with the rest of your project config
- you're comfortable with `lein-githooks` taking control of your
  `.git/hooks` directory 

Do not use `lein-githooks` if:
- you have existing non-lein-githooks-based hooks that you don't want
  to replace - right now `lein-githooks` doesn't like to share.
- some JVM start-up time in key hook points is going to drive you
  mad - right now there's a fair bit of bouncing around through git
  and leiningen involved

## Usage

### Adding lein-githooks to your project

Current dependency information:

    [jainsahab/lein-githooks "1.0.0"]

`lein-githooks` is all about distributing git hooks for your project so
you probably want to install it at project level rather than in your
`profiles.clj` file. Add the plugin dependency and the commands you
would like to run in each git hook as follows.

```clojure
(defproject my-project "0.x.x"

  ;; ...

  :profiles {:dev {:plugins [[lein-githooks "0.1.0"]]
                   :githooks {:auto-install true
                              :pre-push ["lein test"]
                              :pre-commit ["lein eastwood"]
}
```

If you set `:auto-install`, the hooks will be automatically installed
and updated any time you use a leiningen command. The default is
false. You may want to think carefully before switching this on.

You can use other git hook types and / or other leiningen profiles as
you see fit. I'm not really sure of the benefit of deploying other
hook types in this way but there didn't seem to be any reason to
preclude it.

Manual usage is as follows:

    $ lein githooks install 

...will ensure that your .git/hooks directory is updated to match the
plugins declared in your `project.clj` by creating, updating or
deleting hook files as necessary. This happens behind the scenes if
you have `:auto-install` on. 

    $ lein githooks clean

...will delete the hook files corresponding to the hooks declared in
your `project.clj`. You shouldn't generally need this but it can be
useful for temporarily disabling hooks. (Though bear in mind if you
push some that fails hooks you're likely causing a problem for other
developers.)

    $ lein githooks run pre-push

...runs the commands specified in `project.clj` for the pre-push
hook. Use similar commands for other hook types. Useful for testing
hooks. This is actually what the hook files run.

### Opting Out

If you're working on a project that uses `lein-githooks` and has
`:auto-install` on but you would prefer to manage your own hooks.

You can simply override the setting in your leiningen `profiles.clj`
and hook installation will be manual for all your projects.

```clojure
{:user {:githooks ^:replace {:auto-install false}}
```

### CI usage

If you're using any CI tool like Jenkins/GoCD etc. `:auto-install` option 
might cause some unpredictable permission related failures in build pipelines. 
To avoid them, you can provide an enviornment variable name to this plugin whose 
value if present, auto-installation will be skipped by default.

```clojure
  :profiles {:dev {:plugins [[jainsahab/lein-githooks "1.0.0"]]
                   :githooks {:ci-env-variable "CI_BUILD_REF"
                              :auto-install true
                              :pre-push ["lein test"]
                              :pre-commit ["lein eastwood"]
}
```


## Finally

Thanks to [Roy Lines](https://github.com/roylines) for pointing me at
something similar in the javascript universe
([git-pre-hooks](https://github.com/node-modules/git-pre-hooks)) that
provided the inspiration for this. I couldn't find anything similar
out there for Leiningen already but I may have missed something.

Feedback and contributions welcome.

## License

Copyright © 2020 Prateek Jain

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
