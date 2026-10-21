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
An addon for **Applied Energistics 2** that adds the **Infinity Cell**: an ME storage cell that supplies one item of
your choice without limit. Any item from any mod can be imprinted, as long as you can pay for the cell.

There are no modes, no limits to configure and no way to change an imprint later. The cost is the cell itself, and it
is the same for every item - so choosing what your first cell is for is the hard part.

## Features

- **Infinity Cell** - imprint one item; the network can extract it endlessly
- **Infinity Fluid Cell** - the same for one fluid
- **Infinity Energy Cell** (requires Applied Flux) - charge it once and it supplies unlimited FE
- A long material chain starting from AE2's singularity, with two new machines: the **Singularity Compressor** and the
  **Infinity Imprinter**
- In-game documentation in the AE2 guide (hold **G** over an item) - English and Japanese
- JEI support
- Jade / WTHIT / The One Probe: machine progress and what is being imprinted

## Requirements

- Minecraft 26.1.2, NeoForge
- Applied Energistics 2
- GuideME
- Optional: Applied Flux (Infinity Energy Cell)

## For Modpack Makers

Items and fluids can be blacklisted from imprinting with the tag `applied_everything:imprint_blacklist`.
See the [README](https://github.com/nokopi36/applied-everything#for-modpack-makers) for details.

## Known Issues

- Applied Flux 1.0.1 crashes when the creative inventory is opened. This is a bug in Applied Flux, fixed upstream for
  its next release.

## 日本語

**Applied Energistics 2** のアドオンです。好きなアイテムを 1 種類、いくらでも取り出せる ME ストレージセル「**無限セル**」を
追加します。セルを用意できれば、どの MOD のアイテムでも刻印できます。

モードの切り替えも上限の設定もなく、あとから刻印を変えることもできません。コストはセルそのもので、どのアイテムでも同じです。
最初の 1 枚を何に使うかが悩みどころです。

### 内容

- **無限セル** - アイテムを 1 種類刻印すると、ネットワークからいくらでも取り出せる
- **無限流体セル** - 液体版
- **無限エネルギーセル**（Applied Flux が必要）- 一度充電すると、FE を無限に供給する
- AE2 の特異点から始まる長い素材チェーンと、2 つの機械（**特異点圧縮機**と**無限刻印機**）
- AE2 のガイド（アイテムにカーソルを合わせて **G** を長押し）にゲーム内の説明。英語と日本語
- JEI 対応
- Jade / WTHIT / The One Probe で、機械の進み具合と刻印中のものを表示

### 必要なもの

- Minecraft 26.1.2、NeoForge
- Applied Energistics 2
- GuideME
- 任意: Applied Flux（無限エネルギーセル）

### モッドパック制作者向け

タグ `applied_everything:imprint_blacklist` で、刻印できないアイテムや液体を指定できます。
詳しくは [README](https://github.com/nokopi36/applied-everything/blob/main/README.ja.md#モッドパック制作者向け) を見てください。

### 既知の問題

- Applied Flux 1.0.1 は、クリエイティブのインベントリを開くとクラッシュします。Applied Flux 側の不具合で、次の版で直る予定です。
```

## ファイルのアップロード

1. `./gradlew build` を実行し、`build/libs/applied_everything-neoforge-26.1.2-<version>.jar` を作る
2. 「Upload File」で jar を選ぶ

| 項目 | 値 |
|---|---|
| Display name | `Applied Everything <version> (26.1.2)` |
| Release type | Beta（AE2 がベータの間） |
| Game versions | 26.1.2、NeoForge、Client、Server、Java 25 |
| Changelog | `CHANGELOG.md` の該当バージョンの節 |
| Related projects | Applied Energistics 2: Required Dependency / GuideME: Required Dependency / Applied Flux: Optional Dependency / JEI: Optional Dependency / Jade: Optional Dependency |

3. 初回はモデレーションの審査がある（数日かかることがある）。審査が通ったら README にプロジェクトの URL を追記する

## 公開前の確認

- [ ] `gradle.properties` の `mod_version` と `CHANGELOG.md` の版が一致している
- [ ] AE2・GuideME の最新版を入れた状態で、開発環境の外（通常のランチャー）で起動できる
- [ ] Applied Flux あり／なしの両方で起動できる
- [ ] ゲーム内のスクリーンショット（セル、2 つの機械、ガイド）を撮った
