# Applied Everything

[English](README.md)

[Applied Energistics 2](https://github.com/AppliedEnergistics/Applied-Energistics-2) のアドオンです。好きなアイテムを
1 種類、いくらでも取り出せる ME ストレージセル「**無限セル**」を追加します。セルを用意できれば、どの MOD のアイテムでも
刻印できます。

モードの切り替えも上限の設定もなく、あとから刻印を変えることもできません。コストはセルそのもので、どのアイテムでも同じです。

- ダウンロード: [CurseForge](https://www.curseforge.com/minecraft/mc-mods/applied-everything)
- Minecraft 26.1.2 / NeoForge
- 必須: Applied Energistics 2 と GuideME
- 任意: [Applied Flux](https://modrinth.com/mod/appflux) を入れると、FE を無限に供給する無限エネルギーセルが使えるようになる
- Jade・WTHIT・The One Probe で、機械の進み具合が見られる（AE2 の仕組みを使うので、追加の依存はない）
- ゲーム内の説明: AE2 のガイド（アイテムにカーソルを合わせて **G** を長押し）の「Applied Everything」

## モッドパック制作者向け

### 刻印のブラックリスト

アイテムタグ `applied_everything:imprint_blacklist` に入っているアイテムは、無限セルに刻印できません。

初期状態の中身:

| アイテム | 理由 |
|---|---|
| Applied Everything のアイテムすべて | 素材を刻印すると、2 枚目以降のセルのコストを飛ばせてしまうため |
| `ae2:singularity` | 特異点が無限に手に入ると、2 枚目以降のセルが電力だけで作れてしまうため |

データパックの `data/applied_everything/tags/item/imprint_blacklist.json` で変更できます。`values` で追加し、
NeoForge の `remove` で初期値を外します。

```json
{
  "values": [
    "minecraft:nether_star"
  ],
  "remove": [
    "ae2:singularity"
  ]
}
```

この例では、ネザースターを禁止に加え、特異点の禁止を解除します。

`"replace": true` で全体を書き直すより、`remove` を使うのがおすすめです。今後のバージョンで初期値に追加された
アイテムも、禁止されたままになります。

今有効になっている一覧（データパックでの変更を含む）は、次のコマンドで確認できます。

```
/neoforge tags minecraft:item get applied_everything:imprint_blacklist
```

液体のタグ `applied_everything:imprint_blacklist` に入っている液体は、無限流体セルに刻印できません。初期状態は空です。
同じ方法で `data/applied_everything/tags/fluid/imprint_blacklist.json` から変更でき、
`/neoforge tags minecraft:fluid get applied_everything:imprint_blacklist` で確認できます。

通常の状態から変わっているアイテム（エンチャント、名前付き、耐久が減ったもの、中身入りの容器など）は、常に刻印できません。
アイテムの種類ではなく状態で決まるのでタグには含まれず、解除もできません。容器の中身を増やすバグ技を防ぐためです。

### レシピ

作業台のレシピはすべて、`data/applied_everything/recipe/` にある普通のデータパックのレシピです。
`blank_infinity_cell`、`blank_infinity_fluid_cell`、`infinity_core`、`singularity_compressor`、`imprinter`、
`infinity_energy_cell` があります。データパックに同じ名前のファイルを置くと上書きでき、次の内容にすると削除できます。

```json
{
  "neoforge:conditions": [{ "type": "neoforge:never" }]
}
```

KubeJS や CraftTweaker でも、ほかの作業台のレシピと同じように変更できます。特異点圧縮機の処理はレシピではないので、
コストは下のサーバー設定で変えてください。

### サーバー設定

`config/applied_everything-server.toml` で変更できます。ワールドのフォルダの `serverconfig/applied_everything-server.toml`
に置くと、そのワールドだけに効きます。サーバーでは、値がクライアントにも送られます。

| 項目 | 初期値 | 意味 |
|---|---|---|
| `singularity_compressor.singularities_per_compression` | 9 | 圧縮 1 回に使う特異点の数（1〜64） |
| `singularity_compressor.energy_per_compression` | 10,000,000 | 圧縮 1 回の AE |
| `singularity_compressor.base_draw_per_tick` | 5,000 | 加速カードなしの 1 ティックの AE |
| `singularity_compressor.draw_per_card` | 5,000 | 加速カード 1 枚で増える 1 ティックの AE（圧縮 1 回の合計は変わらない） |
| `infinity_imprinter.energy_per_imprint` | 100,000 | 刻印 1 回の AE |
| `infinity_imprinter.ticks_per_imprint` | 100 | 刻印 1 回にかかるティック |
| `infinity_imprinter.energy_cell_charge_rate` | 50,000 | 無限エネルギーセルを充電する 1 ティックの AE |
| `infinity_energy_cell.capacity_fe` | 1,000,000,000 | 無限エネルギーセルが無限になるまでに必要な FE |

- ゲーム内のガイドには、常に初期値が表示されます。
- 途中のワールドで `capacity_fe` を上げると、満タンだった無限エネルギーセルが満タンでなくなり、新しい容量まで充電し直すまで
  FE を供給しなくなります。下げる分には問題ありません。
- 加速カードの最大数（4 枚）は変えられません。

## ライセンス

- コード: [MIT](LICENSE)
- アセット（テクスチャ、モデル、言語ファイル、ガイドのページ、画面の配置）: [CC BY-NC-SA 3.0](LICENSE-ASSETS)。
  一部のテクスチャは Applied Energistics 2 のアセット（同じライセンス）を元にしています。
