# YOMOU CRAWLER
[小説を読もう！][] の更新チェッカー
***
[![Build Status](https://travis-ci.org/hide6644/crawler.svg?branch=master)](https://travis-ci.org/hide6644/crawler)
[![Coverage Status](https://coveralls.io/repos/github/hide6644/crawler/badge.svg?branch=master)](https://coveralls.io/github/hide6644/crawler?branch=master)

## DESCRIPTION
* 更新された小説をメールで通知する
* 更新履歴を含めて小説をデータベースに保存する
* 小説を読もう！のサーバに負荷がかからない様に更新チェックを行う
* 保存した小説を全文検索出来る（予定）

### DATABASE
MySQLでの動作を前提としている。  
データベースを作成たら下記のスクリプトを実行し、テーブルを作成する。

  /crawler/src/config/schema.sql

  /crawler/src/test/resources/jdbc.properties については、自身の環境に合わせて変更すること。

### E-MAIL
登録した小説に更新があった場合、メールで通知するようになっている。

/crawler/src/test/resources/mail.properties については、自身の環境に合わせて変更すること。

## USAGE
```console
Usage: java [VM flags] <command>

VM flags:
  -Dapp_home
      実行フォルダを指定する（キャッシュ、ログ、レポートの保存先になる）
  -jar
      実行Jarのパスを指定する

command:
  add=http://ncode.syosetu.com/小説のURL/
      小説を追加する
  del=http://ncode.syosetu.com/小説のURL/
      小説を削除する
  checkForUpdates
      小説の更新を確認する
  sendReport
      更新の確認結果をメールする

examples:
  ・小説を追加
    java -Dapp_home=/home/crawler -jar /home/crawler/crawler.jar add=http://ncode.syosetu.com/小説のURL/
  ・小説を削除
    java -Dapp_home=/home/crawler -jar /home/crawler/crawler.jar del=http://ncode.syosetu.com/小説のURL/
  ・小説の更新を確認して、その結果をメールで送信
    java -Dapp_home=/home/crawler -jar /home/crawler/crawler.jar checkForUpdates sendReport
```
## LICENSE
YOMOU CRAWLER is released under version 2.0 of the [Apache License][].

[小説を読もう！]: http://yomou.syosetu.com/
[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
