"""Generates the block models (idle and working) of the frame machines: the Singularity Compressor and the Imprinter.

Each is a frame open at the front and back. On the inside of each of the four walls sits a stepped
square pyramid whose tip points at the centre of the block.

Usage: python tools/generate_compressor_model.py src/main/resources/assets/applied_everything/models/block
"""
import json
import sys

NS = 'applied_everything'

# Frame parts: from, to
FRAME = [
    ([0, 0, 0], [16, 2, 16]),    # bottom
    ([0, 14, 0], [16, 16, 16]),  # top
    ([0, 2, 0], [2, 14, 16]),    # left
    ([14, 2, 0], [16, 14, 16]),  # right
]

# Pyramid steps from the wall towards the centre: (width of the square, thickness)
PYRAMID_STEPS = [(6, 1), (4, 1), (2, 1)]

# Each wall's inner face: (axis the pyramid grows along, position of the face, direction towards the centre)
WALLS = [('y', 14, -1), ('y', 2, 1), ('x', 2, 1), ('x', 14, -1)]


def frame_element(a, b):
    faces = {}
    for face in ('north', 'south', 'east', 'west', 'up', 'down'):
        entry = {'texture': '#frame'}
        # Faces on the outside of the block can be culled against neighbours
        if (face == 'down' and a[1] == 0) or (face == 'up' and b[1] == 16) or (face == 'west' and a[0] == 0) \
                or (face == 'east' and b[0] == 16) or (face == 'north' and a[2] == 0) or (face == 'south' and b[2] == 16):
            entry['cullface'] = face
        faces[face] = entry
    return {'from': a, 'to': b, 'faces': faces}


def pyramid_elements(axis, face, direction, texture, glowing):
    elements = []
    level = face
    for width, thickness in PYRAMID_STEPS:
        low, high = 8 - width / 2, 8 + width / 2
        near, far = sorted((level, level + direction * thickness))
        if axis == 'y':
            a, b = [low, near, low], [high, far, high]
        else:
            a, b = [near, low, low], [far, high, high]
        element = {
            'from': a,
            'to': b,
            'faces': {f: {'texture': texture} for f in ('north', 'south', 'east', 'west', 'up', 'down')},
        }
        if glowing:
            element['neoforge_data'] = {'block_light': 15, 'sky_light': 15}
        elements.append(element)
        level += direction * thickness
    return elements


# Machine: textures of its pyramids (the frame is shared)
MACHINES = {
    'singularity_compressor': 'singularity_compressor_pyramid',
    'imprinter': 'imprinter_pyramid',
}


def model(pyramid, working):
    pyramid_texture = '#pyramid_lit' if working else '#pyramid'
    return {
        'parent': 'minecraft:block/block',
        'textures': {
            'particle': f'{NS}:block/singularity_compressor_frame',
            'frame': f'{NS}:block/singularity_compressor_frame',
            'pyramid': f'{NS}:block/{pyramid}',
            'pyramid_lit': f'{NS}:block/{pyramid}_lit',
        },
        'elements': [frame_element(a, b) for a, b in FRAME]
                    + [e for wall in WALLS for e in pyramid_elements(*wall, pyramid_texture, working)],
    }


def main():
    out = sys.argv[1]
    for machine, pyramid in MACHINES.items():
        for name, working in ((machine, False), (f'{machine}_working', True)):
            with open(f'{out}/{name}.json', 'w', encoding='utf-8') as f:
                json.dump(model(pyramid, working), f, indent=2)
                f.write('\n')


if __name__ == '__main__':
    main()
