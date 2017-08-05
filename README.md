# YOMOU CRAWLER
[小説を読もう！][] の更新チェッカー

## DESCRIPTION
* 更新された小説をメールで通知する
* 更新履歴を保存したり、小説を全文検索出来たりする

## USAGE
```console
Usage: java [VM flags] <command>

VM flags:
  -Dapp_home
      実行フォルダを指定する
  -jar
      実行Jarのパスを指定する

command:
  checkForUpdates
      小説の更新を確認する
  sendReport
      更新チェック結果をメールする

examples:
  ・小説を追加
    java -Dapp_home=/home/crawler -jar /home/crawler/crawler.jar http://ncode.syosetu.com/小説のURL/
  ・小説の更新を確認して、その結果をメールで送信
    java -Dapp_home=/home/crawler -jar /home/crawler/crawler.jar checkForUpdates sendReport
```
## LICENSE
YOMOU CRAWLER is released under version 2.0 of the [Apache License][].

[小説を読もう！]: http://yomou.syosetu.com/
[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
