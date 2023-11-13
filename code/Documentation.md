# Documentation lab07 SMT client

## Command line interface

example : ```smtprank (<args> <val>)[]```

Liste of args :

- -i / --ip \<String\>: Setup SMTP server IP
- -p / --port <int>: Setup SMTP port
- -ma / --mailAddress <String> : File path to the mail address group
- -mc / --mailContent <String> : File path to the prank mail content
- -g / --groupe <int> : Select a specific group of mail based on the mailAddress file
- -m / --mail <int> : Select a specific mail content based on the mailContent file
- -h / --help : Display extended help

Full example : ```smtprank -i localhost -p 1025 - ma C:\mailaddress.txt -mc C:\mailcontent.txt -g 1 -m 2```

The arguments order is not important.

