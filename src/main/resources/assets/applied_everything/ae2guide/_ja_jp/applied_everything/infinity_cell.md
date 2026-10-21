---
navigation:
  parent: applied_everything/index.md
  title: 無限セル
  icon: infinity_cell
  position: 30
item_ids:
- blank_infinity_cell
- infinity_cell
- blank_infinity_fluid_cell
- infinity_fluid_cell
---

# 無限セル

<Row>
  <ItemImage id="blank_infinity_cell" scale="4" />
  <ItemImage id="infinity_cell" scale="4" />
</Row>

無限セルは、1 種類のアイテムをいくらでも取り出せるセルです。<ItemLink id="ae2:drive" /> か
<ItemLink id="ae2:me_chest" /> に入れると、ネットワークからはそのアイテムをどれだけ取り出しても減りません。

## 空の無限セル

<RecipeFor id="blank_infinity_cell" />

## 刻印

[無限刻印機](imprinter.md) に、空の無限セルと刻印したいアイテムを入れます。そのアイテムの無限セルができます。

刻印は変更できません。<ItemLink id="ae2:cell_workbench" /> でも扱えません。

次のアイテムは刻印できません。

* Applied Everything 自身のアイテムと、AE2 の <ItemLink id="ae2:singularity" />（特異点が無限に手に入ると、
  2 枚目以降のセルが電力だけで作れてしまうため）。
* 通常の状態から変わっているアイテム: エンチャントされたアイテムや本、名前を付けたアイテム、耐久が減った道具、
  中身入りのシュルカーボックス・バンドル・ストレージセル、染めた防具など。

## ネットワークでの動き

* 刻印したアイテムは、いつでも取り出せます。
* ほかのアイテムは入りません。
* ポータブルセルなど、ほかのセルの中には入れられません。

## 無限流体セル

<Row>
  <ItemImage id="blank_infinity_fluid_cell" scale="4" />
  <ItemImage id="infinity_fluid_cell" scale="4" />
</Row>

液体版の無限セルです。空の無限流体セルは、空の無限セルのアイテムセルハウジングを
<ItemLink id="ae2:fluid_cell_housing" /> に替えて作ります。

<RecipeFor id="blank_infinity_fluid_cell" />

[無限刻印機](imprinter.md) に、空の無限流体セルを入れ、タンクに液体を 1,000 mB 入れると刻印できます。
データ付きの液体は刻印できません。
