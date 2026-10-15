# Applied Everything

オリジナル MOD。仕様は `docs/SPEC.md`、進捗は `docs/PROGRESS.md`。作業開始時に両方を読むこと。

## 環境
- Minecraft 26.1.2 / NeoForge 26.1.2.116 / Java 25
- mod_id: `applied_everything` / パッケージ: `com.nokopi.appliedeverything` / 作者: nokopi
- 連携 MOD: Applied Energistics 2 26.1.13-beta（必須、Maven Central `org.appliedenergistics:appliedenergistics2`）、GuideME 26.1.15-beta（必須、Maven Central `org.appliedenergistics:guideme`）
- ビルド: PATH の `java` は 1.8 なので、`JAVA_HOME` に Gradle が取得した JDK 25 を指定して実行する
  `export JAVA_HOME="$HOME/.gradle/jdks/eclipse_adoptium-25-amd64-windows.2" && ./gradlew build`
- Gradle Wrapper: jar は公式 8.14 系と一致を確認済み（配布 URL は 9.2.1、`distributionSha256Sum` で固定）

## 参考資料（refs/ — 読み取り専用）
`./setup_refs.sh` で取得。コミットは `refs/SOURCES.lock` に固定。

| パス | 用途 |
|---|---|
| `refs/mdk/` | NeoForge 26.1.2 MDK（雛形）。build.gradle の書き方の確認用 |
| `refs/neoforge-docs/docs/` | NeoForge ドキュメント（26.1 版）。登録・Data Components・レシピ・datagen・GameTest |
| `refs/ae2/` | AE2 26.1 のソース。セル API は `src/main/java/appeng/api/storage/cells/`、クリエイティブセルの実装は `appeng/items/storage/CreativeCellItem.java` と `appeng/me/cells/CreativeCell*.java`、ガイドは `guidebook/` |
| `refs/guideme/` | GuideME 26.1 のソース。ガイドの登録方法（「次」の機能で使用） |

## ルール
- `refs/` 配下は編集しない・実行しない。参考にした MOD のコードはコピーせず、書き方の参考にとどめる（AE2 本体は LGPL-3.0、API は MIT。本体コードのコピーは特に避ける）。
- NeoForge・AE2・GuideME の API を記憶で書かない。`refs/` で実在とシグネチャを確認してから使う。見つからなければ推測せず報告する。
- 古い書き方（NBT 直書き、`ResourceLocation`（26.1 は `Identifier`）、`@ObjectHolder`、`net.minecraftforge.*`）を使わない。
- 1 回に 1 機能。計画 → 承認 → 実装 → `./gradlew build` → GameTest（あれば）→ `runClient` 確認手順の提示 → PROGRESS 更新とコミット。
- SPEC にない機能を勝手に足さない。仕様を変えるときは SPEC を先に更新し、理由を `docs/DEVIATIONS.md` に書く。
- 無限セルにモード切り替え・上限変更・設定 GUI を足さない（このMODの差別化の核）。
- 依存の追加・ビルド設定の変更は理由を説明してから行う。
- 数値は定数か config にまとめ、調整しやすくする。
- 言語ファイルは `en_us.json` と `ja_jp.json` の両方を更新する。可能な限りデータ生成で出力する。

## 利用範囲とライセンス
- 公開予定（Modrinth / CurseForge）。ライセンスは未決定のため暫定で `All Rights Reserved`。
- AE2 のアセット（テクスチャ等）を流用しない。AE2 の API は AE2 のライセンスに従って利用する。
- Modrinth は AI 生成コードがかなりの部分を占める場合に申告が必要（2026-08〜）。公開前に最新ルールを確認する。
