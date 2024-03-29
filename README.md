[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![CircleCI](https://circleci.com/gh/hide6644/crawler/tree/master.svg?style=svg)](https://circleci.com/gh/hide6644/crawler/tree/master)
[![Coverage Status](https://coveralls.io/repos/github/hide6644/crawler/badge.svg?branch=master)](https://coveralls.io/github/hide6644/crawler?branch=master)
[![Maintainability](https://api.codeclimate.com/v1/badges/b931ae77c829c4f9d446/maintainability)](https://codeclimate.com/github/hide6644/crawler/maintainability)
***
# YOMOU CRAWLER
[小説を読もう！][] の更新チェッカー

## DESCRIPTION
* 更新された小説をメールで通知する
* 更新履歴を含めて小説をデータベースに保存する
* 小説を読もう！のサーバに負荷がかからない様に更新チェックを行う
* 保存した小説を全文検索出来る（予定）

### DATABASE
MySQLでの動作を前提としている。
データベースを作成し、下記のスクリプトを実行する。

  /crawler/src/config/schema.sql

  /crawler/src/test/resources/jdbc.properties については、自身の環境に合わせて変更すること。

### E-MAIL
登録した小説に更新があった場合、メールで通知出来る。

/crawler/src/test/resources/mail.properties については、自身の環境に合わせて変更すること。

### DEPLOY
```console
任意のフォルダ/crawler.jar
    /lib/ビルドしたときにダウンロードしたJarファイル
```

## USAGE
```console
Usage: java [VM flags] <command>

VM flags:
  -Dapp_home
      実行フォルダを指定する（キャッシュ、ログ、レポートの保存先になる）
  -jar
      crawler.jarのパスを指定する
  -Dhttp_proxy
      任意でプロキシサーバーを指定する（http://hoge:piyo@foo.bar:8080）

command:
  save=http://ncode.syosetu.com/小説のURL/
      小説を登録する
      登録済みの小説の場合、更新を確認する
  del=http://ncode.syosetu.com/小説のURL/
      小説を削除する
  checkForUpdates
      登録済みの全ての小説の更新を確認する
      但し、1年以上更新の無い小説は対象外とする
  sendUnreadReport
      更新の確認結果をメールする
  sendModifiedDateReport
      登録済み小説の一覧(タイトル、最終更新日時)をメールする

examples:
  ・小説を追加
    java -Dapp_home=/home/crawler -jar /home/crawler/crawler.jar save=http://ncode.syosetu.com/小説のURL/
  ・小説を削除
    java -Dapp_home=/home/crawler -jar /home/crawler/crawler.jar del=http://ncode.syosetu.com/小説のURL/
  ・小説の更新を確認して、その結果をメールで送信
    java -Dapp_home=/home/crawler -jar /home/crawler/crawler.jar checkForUpdates sendUnreadReport
```

## LICENSE
YOMOU CRAWLER is released under version 2.0 of the [Apache License][].

[小説を読もう！]: http://yomou.syosetu.com/
[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
