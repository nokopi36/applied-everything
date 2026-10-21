# CurseForge 公開メモ

CurseForge のプロジェクト作成画面に入力する内容と、ファイルをアップロードする手順。

## プロジェクトの設定

| 項目 | 値 |
|---|---|
| Game / Class | Minecraft / Mods |
| Name | Applied Everything |
| Summary | Imprint any item, fluid or energy into an AE2 Infinity Cell for an endless supply - if you can afford the cell. |
| Main category | Applied Energistics 2 |
| Additional categories | Storage、Technology |
| License | Custom（下の「ライセンスの文面」を貼る） |
| Project avatar | `src/main/resources/logo.png`（400×400） |
| Source | https://github.com/nokopi36/applied-everything |
| Issues | https://github.com/nokopi36/applied-everything/issues |

### ライセンスの文面

```
Code: MIT License - https://github.com/nokopi36/applied-everything/blob/main/LICENSE
Assets (textures, models, language files, guide pages, screen layouts): CC BY-NC-SA 3.0 -
https://github.com/nokopi36/applied-everything/blob/main/LICENSE-ASSETS
Some textures are derived from Applied Energistics 2's assets (CC BY-NC-SA 3.0).
```

## 説明文（Description）

CurseForge のエディタに貼る。スクリーンショットは撮影後に差し込む。

```markdown
## English

An addon for **Applied Energistics 2** that adds the **Infinity Cell**: an ME storage cell that supplies one item of
your choice without limit. Any item from any mod can be imprinted, as long as you can pay for the cell.

### Features

- **Infinity Cell** - imprint one item; the network can extract it endlessly
- **Infinity Fluid Cell** - the same for a fluid
- **Infinity Energy Cell** (requires Applied Flux) - charge it once and it supplies unlimited FE
- Materials made from AE2's singularity, and two machines: the **Singularity Compressor** and the **Infinity Imprinter**

### Requirements

- Applied Energistics 2
- GuideME
- Optional: Applied Flux

### For Modpack Makers

Items and fluids can be blacklisted from imprinting with the tag `applied_everything:imprint_blacklist`.
See the [README](https://github.com/nokopi36/applied-everything#for-modpack-makers) for details.

## 日本語

**Applied Energistics 2** のアドオンです。好きなアイテムを 1 種類、いくらでも取り出せる ME ストレージセル「**無限セル**」を
追加します。セルを用意できれば、どの MOD のアイテムでも刻印できます。

### 内容

- **無限セル** - アイテムを 1 種類刻印すると、ネットワークからいくらでも取り出せる
- **無限流体セル** - 液体版
- **無限エネルギーセル**（Applied Flux が必要）- 一度充電すると、FE を無限に供給する
- AE2 の特異点から作成できる素材と、2 つの機械（**特異点圧縮機**と**無限刻印機**）

### 必要なもの

- Applied Energistics 2
- GuideME
- 任意: Applied Flux

### モッドパック制作者向け

タグ `applied_everything:imprint_blacklist` で、刻印できないアイテムや液体を指定できます。
詳しくは [README](https://github.com/nokopi36/applied-everything/blob/main/README.ja.md#モッドパック制作者向け) を見てください。
```

## ファイルのアップロード

1. `./gradlew build` を実行し、`build/libs/applied_everything-neoforge-26.1.2-<version>.jar` を作る
2. 「Upload File」で jar を選ぶ

| 項目 | 値 |
|---|---|
| Display name | `Applied Everything <version> (26.1.2)` |
| Release type | Beta（AE2 がベータの間） |
| Game versions | 26.1.2、NeoForge、Client、Server、Java 25 |
| Changelog | `CHANGELOG.md` と `CHANGELOG.ja.md` の該当バージョンの節（英語、日本語の順に並べる） |
| Related projects | Applied Energistics 2: Required Dependency / GuideME: Required Dependency / Applied Flux: Optional Dependency / JEI: Optional Dependency / Jade: Optional Dependency |

3. 初回はモデレーションの審査がある（数日かかることがある）。審査が通ったら README にプロジェクトの URL を追記する

## 公開前の確認

- [ ] `gradle.properties` の `mod_version` と `CHANGELOG.md`・`CHANGELOG.ja.md` の版が一致している
- [ ] AE2・GuideME の最新版を入れた状態で、開発環境の外（通常のランチャー）で起動できる
- [ ] Applied Flux あり／なしの両方で起動できる
- [ ] ゲーム内のスクリーンショット（セル、2 つの機械、ガイド）を撮った
