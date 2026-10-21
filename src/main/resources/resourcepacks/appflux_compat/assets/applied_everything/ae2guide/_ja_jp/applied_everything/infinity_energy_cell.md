---
navigation:
  parent: applied_everything/index.md
  title: 無限エネルギーセル
  icon: infinity_energy_cell
  position: 40
item_ids:
- infinity_energy_cell
---

# 無限エネルギーセル

<ItemImage id="infinity_energy_cell" scale="4" />

エネルギー版の無限セルです。アドオンの Applied Flux が入っているので使えます。

<RecipeFor id="infinity_energy_cell" />

作ったばかりのセルは、**1,000,000,000 FE**（500,000,000 AE）充電するまで使えません。[無限刻印機](imprinter.md)
（毎ティック 50,000 AE まで）、AE2 の <ItemLink id="ae2:charger" />（毎ティック 10,000 AE まで）、ほかの MOD の FE 充電器で
充電できます。名前に充電の割合が表示されます。

満タンになったら <ItemLink id="ae2:drive" /> に入れてください。Applied Flux を通じて、ネットワークに FE を無限に供給します。
Applied Flux の <ItemLink id="appflux:flux_accessor" /> で、ほかの MOD の機械に送れます。Applied Flux の設定
`flux_accessor.enable` をオンにすると（初期値はオフ）、ME ネットワーク自体の電力にもなります。
