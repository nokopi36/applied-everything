# Applied Everything

[English](README.md)

[Applied Energistics 2](https://github.com/AppliedEnergistics/Applied-Energistics-2) のアドオンです。好きなアイテムを
1 種類、いくらでも取り出せる ME ストレージセル「**無限セル**」を追加します。セルを用意できれば、どの MOD のアイテムでも
刻印できます。

モードの切り替えも上限の設定もなく、あとから刻印を変えることもできません。コストはセルそのもので、どのアイテムでも同じです。

- Minecraft 26.1.2 / NeoForge
- Applied Energistics 2 と GuideME が必要
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

## ライセンス

- コード: [MIT](LICENSE)
- アセット（テクスチャ、モデル、言語ファイル、ガイドのページ、画面の配置）: [CC BY-NC-SA 3.0](LICENSE-ASSETS)。
  一部のテクスチャは Applied Energistics 2 のアセット（同じライセンス）を元にしています。
